package com.taskbuddy.service.impl;

import com.taskbuddy.entity.*;
import com.taskbuddy.repository.BookingRepository;
import com.taskbuddy.repository.UserRepository;
import com.taskbuddy.service.TaskerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskerServiceImpl implements TaskerService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;

    public TaskerServiceImpl(BookingRepository bookingRepo,
                             UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    // 1️⃣ View all pending bookings
    @Override
    public List<Booking> getPendingBookings() {
        return bookingRepo.findByStatus(BookingStatus.PENDING);
    }

    // 2️⃣ Accept booking
    @Override
    public Booking acceptBooking(Long bookingId, String taskerEmail) {

        User tasker = userRepo.findByEmail(taskerEmail)
                .orElseThrow(() -> new RuntimeException("Tasker not found"));

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not available");
        }

        booking.setTasker(tasker);
        booking.setStatus(BookingStatus.ACCEPTED);

        return bookingRepo.save(booking);
    }

    // 3️⃣ Complete booking
    @Override
    public Booking completeBooking(Long bookingId, String taskerEmail) {

        User tasker = userRepo.findByEmail(taskerEmail)
                .orElseThrow(() -> new RuntimeException("Tasker not found"));

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!tasker.equals(booking.getTasker())) {
            throw new RuntimeException("This booking is not assigned to you");
        }

        booking.setStatus(BookingStatus.COMPLETED);

        return bookingRepo.save(booking);
    }
}
