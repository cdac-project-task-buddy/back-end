package com.taskbuddy.controller;

import com.taskbuddy.dto.request.ReviewRequest;
import com.taskbuddy.dto.response.ReviewDTO;
import com.taskbuddy.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ReviewDTO> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(reviewService.createReview(request, authentication.getName()));
    }
    
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, request, authentication.getName()));
    }

    
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<ReviewDTO>> getWorkerReviews(@PathVariable Long workerId) {
        return ResponseEntity.ok(reviewService.getWorkerReviews(workerId));
    }

}