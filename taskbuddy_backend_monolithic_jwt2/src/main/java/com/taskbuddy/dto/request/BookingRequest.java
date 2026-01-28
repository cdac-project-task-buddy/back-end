package com.taskbuddy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingRequest {
    @NotNull(message = "Worker ID is required")
    private Long workerId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    @NotNull(message = "Booking date and time is required")
    private LocalDateTime bookingDate;
    
    @NotNull(message = "Address is required")
    private AddressRequest address;

}