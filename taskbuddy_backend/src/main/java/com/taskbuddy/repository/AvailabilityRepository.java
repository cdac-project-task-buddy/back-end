package com.taskbuddy.repository;

import com.taskbuddy.entities.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByWorkerId(Long workerId);
    List<Availability> findByWorkerIdAndDay(Long workerId, String day);
    List<Availability> findByDeletedFalse();
}