package com.taskbuddy.repository;

import com.taskbuddy.entities.ServiceName;
import com.taskbuddy.entities.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    Optional<Services> findByName(ServiceName name);
    boolean existsByName(ServiceName name);
    List<Services> findByDeletedFalse();
}