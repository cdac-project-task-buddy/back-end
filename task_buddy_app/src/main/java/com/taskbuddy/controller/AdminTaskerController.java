package com.taskbuddy.controller;

import com.taskbuddy.dto.TaskerRequest;
import com.taskbuddy.entity.Role;
import com.taskbuddy.entity.TaskerProfile;
import com.taskbuddy.entity.User;
import com.taskbuddy.repository.TaskerProfileRepository;
import com.taskbuddy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/taskers")
public class AdminTaskerController {

    private final UserRepository userRepository;
    private final TaskerProfileRepository taskerRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminTaskerController(UserRepository userRepository,
                                 TaskerProfileRepository taskerRepo,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskerRepo = taskerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public String createTasker(@RequestBody TaskerRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.TASKER);

        User savedUser = userRepository.save(user);

        TaskerProfile profile = new TaskerProfile();
        profile.setUser(savedUser);
        profile.setSkills(request.getSkills());
        profile.setAvailable(true);

        taskerRepo.save(profile);

        return "Tasker created successfully";
    }
}
