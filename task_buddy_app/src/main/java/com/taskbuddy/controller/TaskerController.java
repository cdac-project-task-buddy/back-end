package com.taskbuddy.controller;

import com.taskbuddy.entity.Booking;
import com.taskbuddy.service.TaskerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasker/bookings")
public class TaskerController {

    private final TaskerService taskerService;

    public TaskerController(TaskerService taskerService) {
        this.taskerService = taskerService;
    }

    // 1️⃣ View pending bookings
    @GetMapping("/pending")
    public List<Booking> viewPendingBookings() {
        return taskerService.getPendingBookings();
    }

    // 2️⃣ Accept booking
    @PutMapping("/{id}/accept")
    public Booking acceptBooking(@PathVariable Long id,
                                 Authentication authentication) {

        String email = authentication.getName();
        return taskerService.acceptBooking(id, email);
    }

    // 3️⃣ Complete booking
    @PutMapping("/{id}/complete")
    public Booking completeBooking(@PathVariable Long id,
                                   Authentication authentication) {

        String email = authentication.getName();
        return taskerService.completeBooking(id, email);
    }
}
