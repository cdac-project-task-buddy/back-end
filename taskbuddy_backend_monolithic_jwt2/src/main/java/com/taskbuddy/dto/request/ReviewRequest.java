package com.taskbuddy.dto.request;

import com.taskbuddy.entities.UserRole;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class ReviewRequest {
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    
    private String comment;
}