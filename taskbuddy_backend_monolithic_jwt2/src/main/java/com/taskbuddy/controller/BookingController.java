package com.taskbuddy.controller;

import com.taskbuddy.dto.request.BookingRequest;
import com.taskbuddy.dto.response.ApiResponse;
import com.taskbuddy.dto.response.BookingResponse;
import com.taskbuddy.entities.Status;
import com.taskbuddy.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(bookingService.createBooking(request, authentication.getName()));
    }
    
    @GetMapping("/my-bookings")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(
            Authentication authentication
    ) {
        List<BookingResponse> bookings =
                bookingService.getCustomerBookings(authentication.getName());

        String message = bookings.isEmpty()
                ? "No bookings found"
                : "Bookings fetched successfully";

        return ResponseEntity.ok(
                new ApiResponse<>(message, bookings)
        );
    }

    
    @GetMapping("/worker-bookings")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<List<BookingResponse>> getWorkerBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getWorkerBookings(authentication.getName()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            Authentication authentication
    ) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status, authentication.getName()));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(
            @PathVariable Long id,
            Authentication authentication
    ) {
        bookingService.deleteBooking(id, authentication.getName());

        return ResponseEntity.ok(
            new ApiResponse<>("Booking deleted successfully", null)
        );
    }
    
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, authentication.getName()));
    }


}