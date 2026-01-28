package com.taskbuddy.service;

import com.taskbuddy.dto.request.BookingRequest;
import com.taskbuddy.dto.response.AddressDTO;
import com.taskbuddy.dto.response.BookingResponse;
import com.taskbuddy.dto.response.ReviewDTO;
import com.taskbuddy.entities.*;
import com.taskbuddy.exception.ResourceNotFoundException;
import com.taskbuddy.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final WorkerRepository workerRepository;
    private final ServicesRepository servicesRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    @Transactional
    public BookingResponse createBooking(BookingRequest request, String email) {
        // Get customer
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Customer customer = customerRepository.findByUserDetailsId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        // Get worker
        Worker worker = workerRepository.findById(request.getWorkerId())
            .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
        
        // Get service
        Services service = servicesRepository.findById(request.getServiceId())
            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        
        // ✅ Create address from request
        Address address = new Address();
        address.setStreet(request.getAddress().getStreet());
        address.setArea(request.getAddress().getArea());
        address.setCity(request.getAddress().getCity());
        address.setState(request.getAddress().getState());
        address.setPincode(request.getAddress().getPincode());
        address.setCountry(request.getAddress().getCountry());
        
        // ✅ Save address (cascade will handle this, but explicit save is clearer)
        Address savedAddress = addressRepository.save(address); // Optional if using cascade
        
        // Create booking
        Booking booking = new Booking();
        booking.setMyCustomer(customer);
        booking.setMyWorker(worker);
        booking.setMyService(service);
        booking.setBookingDate(request.getBookingDate());
        booking.setMyAddress(savedAddress);
        booking.setStatus(Status.PENDING);
        booking.setPrice(BigDecimal.valueOf(worker.getFees()));
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Return response
        return mapToBookingResponse(savedBooking);
    }

//    public BookingResponse createBooking(BookingRequest request, String customerEmail) {
//        User user = userRepository.findByEmailAndDeletedFalse(customerEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        
//        Customer customer = customerRepository.findByUserDetailsId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
//        
//        Worker worker = workerRepository.findById(request.getWorkerId())
//                .orElseThrow(() -> new RuntimeException("Worker not found"));
//        
//        if (!worker.isAvailablity()) {
//            throw new RuntimeException("Worker is not available");
//        }
//        
//        Services service = servicesRepository.findById(request.getServiceId())
//                .orElseThrow(() -> new RuntimeException("Service not found"));
//        
//        Address address = addressRepository.findById(request.getAddressId())
//                .orElseThrow(() -> new RuntimeException("Address not found"));
//        
//        // Check if worker already has a booking at this time
//        if (bookingRepository.existsByWorkerAndDateTime(worker.getId(), request.getBookingDate())) {
//            throw new RuntimeException("Worker is already booked at this time");
//        }
//        
//        Booking booking = new Booking();
//        booking.setMyWorker(worker);
//        booking.setMyService(service);
//        booking.setMyCustomer(customer);
//        booking.setMyAddress(address);
//        booking.setBookingDate(request.getBookingDate());
//        booking.setPrice(service.getBasePrice());
//        booking.setStatus(Status.PENDING);
//        
//        booking = bookingRepository.save(booking);
//        
//        return mapToBookingResponse(booking);
//    }
    
    
    @Transactional
    public List<BookingResponse> getCustomerBookings(String customerEmail) {
        User user = userRepository.findByEmailAndDeletedFalse(customerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Customer customer = customerRepository.findByUserDetailsId(user.getId())
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
        
        return bookingRepository.findByMyCustomerId(customer.getId())
                .stream()
                .filter(b -> !b.isDeleted()) 
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    
    @Transactional
    public List<BookingResponse> getWorkerBookings(String workerEmail) {
        User user = userRepository.findByEmailAndDeletedFalse(workerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Worker worker = workerRepository.findByUserDetailsId(user.getId())
                .orElseThrow(() -> new RuntimeException("Worker profile not found"));
        
        return bookingRepository.findByMyWorkerId(worker.getId())
                .stream()
                .filter(b -> !b.isDeleted())  
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public BookingResponse updateBookingStatus(Long bookingId, Status status, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        User user = userRepository.findByEmailAndDeletedFalse(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify user owns this booking
        if (user.getUserRole() == UserRole.ROLE_WORKER) {
            Worker worker = workerRepository.findByUserDetailsId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Worker not found"));
            if (!booking.getMyWorker().getId().equals(worker.getId())) {
                throw new RuntimeException("You can only update your own bookings");
            }
        }
        
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        
        return mapToBookingResponse(booking);
    }
    
    
    @Transactional
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        return mapToBookingResponse(booking);
    }
    
    @Transactional
    public void deleteBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        User user = userRepository.findByEmailAndDeletedFalse(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Customer customer = customerRepository.findByUserDetailsId(user.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (!booking.getMyCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("You can only delete your own bookings");
        }
        
        // Allow deleting CANCELLED bookings too
        if (booking.getStatus() != Status.PENDING && 
            booking.getStatus() != Status.BOOKED && 
            booking.getStatus() != Status.CANCELLED) {
            throw new RuntimeException("Cannot delete booking in current status");
        }
        
        booking.setDeleted(true);
        bookingRepository.save(booking);
    }
    
    
    
    @Transactional
    private BookingResponse mapToBookingResponse(Booking booking) {
        BookingResponse response = BookingResponse.builder()
                .id(booking.getId())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus().toString())
                .price(booking.getPrice())
                .build();
        
        if (booking.getMyWorker() != null) {
            response.setWorkerId(booking.getMyWorker().getId());
            User workerUser = booking.getMyWorker().getUserDetails();
            if (workerUser != null) {
                response.setWorkerName(workerUser.getFirstName() + " " + workerUser.getLastName());
                response.setWorkerPhone(workerUser.getPhone());
            }
        }
        
        if (booking.getMyService() != null) {
            response.setServiceId(booking.getMyService().getId());
            response.setServiceName(booking.getMyService().getName());
        }
        
        if (booking.getMyCustomer() != null) {
            response.setCustomerId(booking.getMyCustomer().getId());
            User customerUser = booking.getMyCustomer().getUserDetails();
            if (customerUser != null) {
                response.setCustomerName(customerUser.getFirstName() + " " + customerUser.getLastName());
                response.setCustomerPhone(customerUser.getPhone());
            }
        }
        
        if (booking.getMyAddress() != null) {
            response.setAddress(modelMapper.map(booking.getMyAddress(), AddressDTO.class));
        }
        
        if (booking.getReview() != null) {
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .id(booking.getReview().getId())
                    .rating(booking.getReview().getRating())
                    .comment(booking.getReview().getComment())
                    .build();
            response.setReview(reviewDTO);
        }
        
        return response;
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId, String customerEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        User user = userRepository.findByEmailAndDeletedFalse(customerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Customer customer = customerRepository.findByUserDetailsId(user.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (!booking.getMyCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("You can only cancel your own bookings");
        }
        
        if (booking.getStatus() != Status.PENDING && booking.getStatus() != Status.BOOKED) {
            throw new RuntimeException("Cannot cancel booking in current status");
        }
        
        booking.setStatus(Status.CANCELLED);
        booking = bookingRepository.save(booking);
        
        return mapToBookingResponse(booking);
    }

}