package com.taskbuddy.controller;

import com.taskbuddy.dto.ReviewRequest;
import com.taskbuddy.entity.Review;
import com.taskbuddy.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review addReview(@RequestBody ReviewRequest request,
                            Authentication authentication) {

        String email = authentication.getName();
        return reviewService.addReview(request, email);
    }
}
