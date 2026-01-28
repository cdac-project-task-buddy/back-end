package com.taskbuddy.service;

import com.taskbuddy.dto.request.LoginRequest;
import com.taskbuddy.dto.request.RegisterRequest;
import com.taskbuddy.dto.response.AuthResponse;
import com.taskbuddy.entities.Customer;
import com.taskbuddy.entities.User;
import com.taskbuddy.entities.UserRole;
import com.taskbuddy.entities.Worker;
import com.taskbuddy.repository.CustomerRepository;
import com.taskbuddy.repository.UserRepository;
import com.taskbuddy.repository.WorkerRepository;
import com.taskbuddy.security.CustomUserDetailsServiceImpl;
import com.taskbuddy.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsServiceImpl userDetailsService;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }
        
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(request.getUserRole() != null ? request.getUserRole() : UserRole.ROLE_CUSTOMER);
        
        user = userRepository.save(user);
        
        // Create Worker or Customer profile
        if (user.getUserRole() == UserRole.ROLE_WORKER) {
            Worker worker = new Worker();
            worker.setUserDetails(user);
            workerRepository.save(worker);
        } else if (user.getUserRole() == UserRole.ROLE_CUSTOMER) {
            Customer customer = new Customer();
            customer.setUserDetails(user);
            customerRepository.save(customer);
        }
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        
        
        return AuthResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userRole(user.getUserRole())
                .build();
    }
    
    
    
    @Transactional
    public AuthResponse login(LoginRequest request) {
    	try {
		    authenticationManager.authenticate(
		    		
		            new UsernamePasswordAuthenticationToken(
		                    request.getEmail(),
		                    request.getPassword()
		            )
		    );
		    
		    User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
		            .orElseThrow(() -> new RuntimeException("User not found"));
		    
		    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		    String jwtToken = jwtService.generateToken(userDetails);
		    
		    
		    return AuthResponse.builder()
		            .token(jwtToken)
		            
		            .userId(user.getId())
		            .email(user.getEmail())
		            .firstName(user.getFirstName())
		            .lastName(user.getLastName())
		            .userRole(user.getUserRole())
		            .build();
		} catch (BadCredentialsException ex) {
		    throw new ResponseStatusException(
		            HttpStatus.UNAUTHORIZED, "Invalid email or password"
		        );
		    }
    }
}