package com.taskbuddy.repository;

import com.taskbuddy.entities.Booking;
import com.taskbuddy.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByMyCustomerId(Long customerId);
    List<Booking> findByMyWorkerId(Long workerId);
    List<Booking> findByStatus(Status status);
    
    @Query("SELECT b FROM Booking b WHERE b.myWorker.id = :workerId " +
           "AND b.bookingDate BETWEEN :start AND :end AND b.deleted = false")
    List<Booking> findWorkerBookingsBetween(
        @Param("workerId") Long workerId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.myWorker.id = :workerId " +
           "AND b.bookingDate = :bookingDate AND b.deleted = false")
    boolean existsByWorkerAndDateTime(
        @Param("workerId") Long workerId,
        @Param("bookingDate") LocalDateTime bookingDate
    );
    
    List<Booking> findByDeletedFalse();
}