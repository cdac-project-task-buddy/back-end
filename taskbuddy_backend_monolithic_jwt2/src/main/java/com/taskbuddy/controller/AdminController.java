package com.taskbuddy.controller;

import com.taskbuddy.dto.response.BookingResponse;
import com.taskbuddy.dto.response.CustomerDTO;
import com.taskbuddy.dto.response.ReviewDTO;
import com.taskbuddy.dto.response.WorkerDTO;
import com.taskbuddy.entities.Booking;
import com.taskbuddy.entities.Customer;
import com.taskbuddy.entities.Review;
import com.taskbuddy.entities.Status;
import com.taskbuddy.entities.Worker;
import com.taskbuddy.repository.BookingRepository;
import com.taskbuddy.repository.CustomerRepository;
import com.taskbuddy.repository.ReviewRepository;
import com.taskbuddy.repository.WorkerRepository;
import com.taskbuddy.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final CustomerRepository customerRepository;
    private final WorkerRepository workerRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final WorkerService workerService;
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("users", customerRepository.findAll().size());
        stats.put("workers", workerRepository.findAll().size());
        stats.put("bookings", bookingRepository.findAll().size());
        stats.put("pendingWorkers", workerRepository.findByVerifiedFalse().size());
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/users")
    @Transactional
    public ResponseEntity<List<CustomerDTO>> getAllUsers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(this::mapToCustomerDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDTOs);
    }
    
    @GetMapping("/workers") 
    @Transactional
    public ResponseEntity<List<WorkerDTO>> getAllWorkers() {
        List<Worker> workers = workerRepository.findAll();
        List<WorkerDTO> workerDTOs = workers.stream()
                .map(this::mapToWorkerDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workerDTOs);
    }
    
    @PatchMapping("/workers/{workerId}/verify")
    public ResponseEntity<?> verifyWorker(@PathVariable Long workerId) {
        workerService.verifyWorker(workerId);
        return ResponseEntity.ok("Worker verified successfully");
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        customerRepository.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
    
    @DeleteMapping("/workers/{workerId}")
    public ResponseEntity<?> deleteWorker(@PathVariable Long workerId) {
        workerRepository.deleteById(workerId);
        return ResponseEntity.ok("Worker deleted successfully");
    }
    
    @GetMapping("/bookings")
    @Transactional
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingResponse> bookingDTOs = bookings.stream()
                .map(this::mapToBookingDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingDTOs);
    }
    
    @PatchMapping("/bookings/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Status.valueOf(status));
        bookingRepository.save(booking);
        return ResponseEntity.ok("Booking status updated");
    }
    
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
        bookingRepository.deleteById(bookingId);
        return ResponseEntity.ok("Booking deleted successfully");
    }
    
    @GetMapping("/reviews")
    @Transactional
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(this::mapToReviewDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDTOs);
    }
    
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
    
    // Mapping methods
    private CustomerDTO mapToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getUserDetails().getFirstName())
                .lastName(customer.getUserDetails().getLastName())
                .email(customer.getUserDetails().getEmail())
                .phone(customer.getUserDetails().getPhone())
                .build();
    }
    
    private WorkerDTO mapToWorkerDTO(Worker worker) {
        return WorkerDTO.builder()
                .id(worker.getId())
                .firstName(worker.getUserDetails().getFirstName())
                .lastName(worker.getUserDetails().getLastName())
                .email(worker.getUserDetails().getEmail())
                .phone(worker.getUserDetails().getPhone())
                .experienceInYears(worker.getExperienceInYears())
                .verified(worker.getVerified())
                .build();
    }
    
    private BookingResponse mapToBookingDTO(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .serviceName(booking.getMyService().getName())
                .customerName(booking.getMyCustomer().getUserDetails().getFirstName() + " " + 
                           booking.getMyCustomer().getUserDetails().getLastName())
                .workerName(booking.getMyWorker().getUserDetails().getFirstName() + " " + 
                          booking.getMyWorker().getUserDetails().getLastName())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus().toString())
                .build();
    }
    
    private ReviewDTO mapToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .customerName(review.getBooking().getMyCustomer().getUserDetails().getFirstName() + " " + 
                            review.getBooking().getMyCustomer().getUserDetails().getLastName())
                .workerName(review.getBooking().getMyWorker().getUserDetails().getFirstName() + " " + 
                          review.getBooking().getMyWorker().getUserDetails().getLastName())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}
