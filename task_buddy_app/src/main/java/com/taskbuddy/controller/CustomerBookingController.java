package com.taskbuddy.controller;

import com.taskbuddy.dto.BookingRequest;
import com.taskbuddy.entity.Booking;
import com.taskbuddy.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/bookings")
public class CustomerBookingController {

    private final BookingService bookingService;

    public CustomerBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // CREATE BOOKING
    @PostMapping
    public Booking createBooking(@RequestBody BookingRequest request,
                                 Authentication authentication) {

        String email = authentication.getName();
        return bookingService.createBooking(request, email);
    }

    // VIEW MY BOOKINGS
    @GetMapping
    public List<Booking> myBookings(Authentication authentication) {

        String email = authentication.getName();
        return bookingService.getCustomerBookings(email);
    }
}
