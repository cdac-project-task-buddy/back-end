package com.taskbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

import com.taskbuddy.entities.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AvailabilityDTO {
    private Long id;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
}