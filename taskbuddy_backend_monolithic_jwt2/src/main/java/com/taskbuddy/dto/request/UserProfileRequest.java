package com.taskbuddy.dto.request;

import com.taskbuddy.dto.response.AddressDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserProfileRequest {
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    @Pattern(regexp = "^[0-9]{10,14}$", message = "Phone must be 10-14 digits")
    private String phone;
    
    private AddressDTO address;

}
