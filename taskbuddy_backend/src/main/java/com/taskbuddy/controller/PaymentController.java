package com.taskbuddy.controller;

import com.razorpay.RazorpayException;
import com.taskbuddy.dto.request.PaymentRequest;
import com.taskbuddy.dto.request.PaymentVerificationRequest;
import com.taskbuddy.dto.response.ApiResponse;
import com.taskbuddy.dto.response.PaymentOrderResponse;
import com.taskbuddy.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
// @CrossOrigin(origins = "*")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/create-order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<PaymentOrderResponse>> createPaymentOrder(
            @Valid @RequestBody PaymentRequest request
    ) {
        try {
            PaymentOrderResponse response = paymentService.createPaymentOrder(request);
            return ResponseEntity.ok(
                new ApiResponse<>("Payment order created successfully", response)
            );
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("Failed to create payment order: " + e.getMessage(), null)
            );
        }
    }
    
    @PostMapping("/verify")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> verifyPayment(
            @Valid @RequestBody PaymentVerificationRequest request
    ) {
        try {
            boolean isVerified = paymentService.verifyPayment(request);
            if (isVerified) {
                return ResponseEntity.ok(
                    new ApiResponse<>("Payment verified successfully", null)
                );
            } else {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Payment verification failed", null)
                );
            }
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("Payment verification error: " + e.getMessage(), null)
            );
        }
    }
}