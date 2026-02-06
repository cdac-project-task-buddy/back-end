package com.taskbuddy.service;

import com.taskbuddy.dto.request.ReviewRequest;
import com.taskbuddy.dto.response.ReviewDTO;
import com.taskbuddy.entities.Booking;
import com.taskbuddy.entities.Review;
import com.taskbuddy.entities.Status;
import com.taskbuddy.entities.Worker;
import com.taskbuddy.exception.ResourceNotFoundException;
import com.taskbuddy.repository.BookingRepository;
import com.taskbuddy.repository.ReviewRepository;
import com.taskbuddy.repository.WorkerRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final WorkerRepository workerRepository;
    
    @Transactional
    public ReviewDTO createReview(ReviewRequest request, String customerEmail) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() != Status.COMPLETED) {
            throw new RuntimeException("Can only review completed bookings");
        }
        
        if (reviewRepository.findByBookingId(request.getBookingId()).isPresent()) {
            throw new RuntimeException("Review already exists for this booking");
        }
        
        Review review = new Review();
        review.setBooking(booking);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        review = reviewRepository.save(review);
        
        // Update worker rating
        updateWorkerRating(booking.getMyWorker().getId());
        
        return mapToReviewDTO(review);
    }
    
//    public List<ReviewDTO> getWorkerReviews(Long workerId) {
//        return reviewRepository.findByWorkerId(workerId)
//                .stream()
//                .map(this::mapToReviewDTO)
//                .collect(Collectors.toList());
//    }
    
    
    @Transactional
    private void updateWorkerRating(Long workerId) {
        List<Review> reviews = reviewRepository.findByWorkerId(workerId);
        
        if (!reviews.isEmpty()) {
            double avgRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            
            Worker worker = workerRepository.findById(workerId)
                    .orElseThrow(() -> new RuntimeException("Worker not found"));
            
            worker.setRating(avgRating);
            workerRepository.save(worker);
        }
    }
    
    
    @Transactional
    private ReviewDTO mapToReviewDTO(Review review) {
        ReviewDTO dto = ReviewDTO.builder()
                .id(review.getId())
                .bookingId(review.getBooking().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
        
        if (review.getBooking() != null) {
            if (review.getBooking().getMyCustomer() != null) {
                var customer = review.getBooking().getMyCustomer().getUserDetails();
                dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
            }
            if (review.getBooking().getMyWorker() != null) {
                var worker = review.getBooking().getMyWorker().getUserDetails();
                dto.setWorkerName(worker.getFirstName() + " " + worker.getLastName());
            }
        }
        
        return dto;
    }

    
    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewRequest request, String email) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        // Verify the review belongs to the authenticated customer
        if (!review.getBooking().getMyCustomer().getUserDetails().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only update your own reviews");
        }
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review updated = reviewRepository.save(review);
        return mapToReviewDTO(updated);
    }

    public List<ReviewDTO> getWorkerReviews(Long workerId) {
        List<Review> reviews = reviewRepository.findByWorkerId(workerId);
        return reviews.stream()
                .map(review -> mapToReviewDTO(review))
                .collect(Collectors.toList());
    }


}