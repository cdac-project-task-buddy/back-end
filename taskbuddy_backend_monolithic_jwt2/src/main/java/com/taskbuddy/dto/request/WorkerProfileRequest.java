package com.taskbuddy.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.taskbuddy.entities.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkerProfileRequest {
    @Min(0)
    private int experienceInYears;
    
    @Min(0)
    private int fees;
    
    private List serviceIds;
    private AddressRequest address;
}