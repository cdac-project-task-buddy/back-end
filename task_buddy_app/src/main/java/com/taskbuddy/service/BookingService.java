package com.taskbuddy.service;

import com.taskbuddy.dto.BookingRequest;
import com.taskbuddy.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingRequest request, String customerEmail);

    List<Booking> getCustomerBookings(String customerEmail);
}
