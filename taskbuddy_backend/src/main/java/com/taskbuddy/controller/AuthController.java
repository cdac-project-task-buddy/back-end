package com.taskbuddy.controller;

import com.taskbuddy.dto.request.LoginRequest;
import com.taskbuddy.dto.request.RegisterRequest;
import com.taskbuddy.dto.response.AuthResponse;
import com.taskbuddy.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    	 try {
    	        return ResponseEntity.ok(authService.login(request));
    	    } catch (Exception e) {
    	        e.printStackTrace(); // 🔥 THIS WILL SHOW THE REAL ERROR
    	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    	    }
    }
}