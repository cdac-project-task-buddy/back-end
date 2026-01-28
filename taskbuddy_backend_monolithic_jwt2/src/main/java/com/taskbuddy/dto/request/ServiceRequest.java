package com.taskbuddy.dto.request;

import com.taskbuddy.entities.ServiceName;
import com.taskbuddy.entities.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceRequest {
    @NotNull
    private ServiceName name;
    
    private String description;
    
    @NotNull
    private BigDecimal basePrice;
}