package com.taskbuddy.service;

import com.taskbuddy.dto.ReviewRequest;
import com.taskbuddy.entity.Review;

public interface ReviewService {
    Review addReview(ReviewRequest request, String customerEmail);
}
