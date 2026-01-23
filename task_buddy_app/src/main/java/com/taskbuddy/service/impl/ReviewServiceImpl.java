package com.taskbuddy.service.impl;

import com.taskbuddy.dto.ReviewRequest;
import com.taskbuddy.entity.*;
import com.taskbuddy.repository.*;
import com.taskbuddy.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final BookingRepository bookingRepo;
    private final TaskerProfileRepository taskerProfileRepo;

    public ReviewServiceImpl(ReviewRepository reviewRepo,
                             BookingRepository bookingRepo,
                             TaskerProfileRepository taskerProfileRepo) {
        this.reviewRepo = reviewRepo;
        this.bookingRepo = bookingRepo;
        this.taskerProfileRepo = taskerProfileRepo;
    }

    @Override
    public Review addReview(ReviewRequest request, String customerEmail) {

        Booking booking = bookingRepo.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 1️⃣ Validate booking status
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Review allowed only after completion");
        }

        // 2️⃣ One review per booking
        if (reviewRepo.existsByBookingId(booking.getId())) {
            throw new RuntimeException("Review already exists");
        }

        // 3️⃣ Save review
        Review review = new Review();
        review.setBooking(booking);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepo.save(review);

        // 4️⃣ Update tasker rating
        TaskerProfile taskerProfile =
                taskerProfileRepo.findAll()
                        .stream()
                        .filter(tp -> tp.getUser().equals(booking.getTasker()))
                        .findFirst()
                        .orElse(null);

        if (taskerProfile != null) {
            double newRating =
                    (taskerProfile.getRating() + request.getRating()) / 2;
            taskerProfile.setRating(newRating);
            taskerProfileRepo.save(taskerProfile);
        }

        return savedReview;
    }
}
