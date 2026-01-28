package com.taskbuddy.controller;

import com.taskbuddy.dto.request.UserProfileRequest;
import com.taskbuddy.dto.response.UserDTO;
import com.taskbuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication.getName()));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @Valid @RequestBody UserProfileRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(userService.updateProfile(request, authentication.getName()));
    }
}
