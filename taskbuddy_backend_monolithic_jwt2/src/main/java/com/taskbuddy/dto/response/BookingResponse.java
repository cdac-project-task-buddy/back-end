package com.taskbuddy.dto.response;

import com.taskbuddy.entities.ServiceName;
import com.taskbuddy.entities.Status;
import com.taskbuddy.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingResponse {
    private Long id;
    private LocalDateTime bookingDate;
    private String status;
    private BigDecimal price;
    
    private Long workerId;
    private String workerName;
    private String workerPhone;
    
    private Long serviceId;
    private ServiceName serviceName;
    
    private Long customerId;
    private String customerName;
    private String customerPhone;
    
    private AddressDTO address;
    private ReviewDTO review;

}