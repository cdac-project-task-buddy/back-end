package com.taskbuddy.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.taskbuddy.entities.User;
import com.taskbuddy.entities.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<User> findByUserRole(UserRole userRole);
}
