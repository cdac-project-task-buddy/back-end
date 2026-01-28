package com.taskbuddy.dto.response;

import com.taskbuddy.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private Long bookingId;
    private Integer rating;
    private String comment;
    private String customerName;
    private String workerName;
}