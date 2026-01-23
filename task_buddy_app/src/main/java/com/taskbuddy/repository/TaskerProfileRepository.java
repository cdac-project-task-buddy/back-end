package com.taskbuddy.repository;

import com.taskbuddy.entity.TaskerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskerProfileRepository extends JpaRepository<TaskerProfile, Long> {
}
