package com.taskbuddy.service.impl;

import com.taskbuddy.dto.BookingRequest;
import com.taskbuddy.entity.*;
import com.taskbuddy.repository.*;
import com.taskbuddy.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ServiceCategoryRepository serviceRepo;

    public BookingServiceImpl(BookingRepository bookingRepo,
                              UserRepository userRepo,
                              ServiceCategoryRepository serviceRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.serviceRepo = serviceRepo;
    }

    @Override
    public Booking createBooking(BookingRequest request, String customerEmail) {

        User customer = userRepo.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ServiceCategory service = serviceRepo.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setService(service);
        booking.setAddress(request.getAddress());
        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(BookingStatus.PENDING);
        booking.setPrice(service.getBasePrice());

        return bookingRepo.save(booking);
    }

    @Override
    public List<Booking> getCustomerBookings(String customerEmail) {

        User customer = userRepo.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return bookingRepo.findByCustomer(customer);
    }
}
