package com.taskbuddy.service;

import com.taskbuddy.entity.Booking;

import java.util.List;

public interface TaskerService {

    List<Booking> getPendingBookings();

    Booking acceptBooking(Long bookingId, String taskerEmail);

    Booking completeBooking(Long bookingId, String taskerEmail);
}
