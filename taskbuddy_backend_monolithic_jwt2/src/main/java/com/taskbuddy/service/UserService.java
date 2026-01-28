package com.taskbuddy.service;



import com.taskbuddy.dto.request.UserProfileRequest;
import com.taskbuddy.dto.response.AddressDTO;
import com.taskbuddy.dto.response.UserDTO;
import com.taskbuddy.entities.Customer;
import com.taskbuddy.entities.User;
import com.taskbuddy.entities.Worker;
import com.taskbuddy.repository.CustomerRepository;
import com.taskbuddy.repository.UserRepository;
import com.taskbuddy.repository.WorkerRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final WorkerRepository workerRepository;
    private final ModelMapper modelMapper;
    
    @Transactional(readOnly = true)
    public UserDTO getProfile(String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        
        // Get address from Customer or Worker
        Customer customer = customerRepository.findByUserDetailsId(user.getId()).orElse(null);
        if (customer != null && customer.getAddress() != null) {
            dto.setAddress(modelMapper.map(customer.getAddress(), AddressDTO.class));
        }
        
        Worker worker = workerRepository.findByUserDetailsId(user.getId()).orElse(null);
        if (worker != null && worker.getAddress() != null) {
            dto.setAddress(modelMapper.map(worker.getAddress(), AddressDTO.class));
        }
        
        return dto;
    }

    
    @Transactional
    public UserDTO updateProfile(UserProfileRequest request, String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        
        user = userRepository.save(user);
        
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}
