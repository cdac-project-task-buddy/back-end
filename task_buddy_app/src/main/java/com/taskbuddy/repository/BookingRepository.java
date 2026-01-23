package com.taskbuddy.repository;

import com.taskbuddy.entity.Booking;
import com.taskbuddy.entity.BookingStatus;
import com.taskbuddy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer(User customer);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByTasker(User tasker);

}
