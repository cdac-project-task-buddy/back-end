

package com.taskbuddy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressRequest {
    @NotBlank
    private String street;
    
    private String area;
    
    @NotBlank
    private String city;
    
    @NotBlank
    private String state;
    
    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits")
    private String pincode;
    
    private String country = "India";
}