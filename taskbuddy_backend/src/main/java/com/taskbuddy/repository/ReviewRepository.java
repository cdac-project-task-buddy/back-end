package com.taskbuddy.repository;

import com.taskbuddy.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional findByBookingId(Long bookingId);
    
    @Query("SELECT r FROM Review r WHERE r.booking.myWorker.id = :workerId AND r.deleted = false")
    List findByWorkerId(@Param("workerId") Long workerId);
    
    List findByDeletedFalse();
}