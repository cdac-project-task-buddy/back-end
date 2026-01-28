package com.taskbuddy.dto.response;

import com.taskbuddy.entities.ServiceName;
import com.taskbuddy.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceDTO {
    private Long id;
    private ServiceName name;
    private String description;
    private BigDecimal basePrice;
}