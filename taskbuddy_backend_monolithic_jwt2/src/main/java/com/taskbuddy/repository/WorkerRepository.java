package com.taskbuddy.repository;

import com.taskbuddy.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUserDetailsId(Long userId);
    
    @Query("SELECT w FROM Worker w JOIN w.services s WHERE s.id = :serviceId AND w.deleted = false")
    List<Worker> findByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT w FROM Worker w WHERE w.rating >= :minRating AND w.deleted = false ORDER BY w.rating DESC")
    List<Worker> findByMinRating(@Param("minRating") double minRating);
    
    @Query("SELECT w FROM Worker w WHERE w.availablity = true AND w.deleted = false")
    List<Worker> findAvailableWorkers();
    
    List<Worker> findByDeletedFalse();
    
    @Query("SELECT w FROM Worker w LEFT JOIN FETCH w.services")
    List<Worker> findAllWithServices();
    
    
    List<Worker> findByVerifiedFalse();
}