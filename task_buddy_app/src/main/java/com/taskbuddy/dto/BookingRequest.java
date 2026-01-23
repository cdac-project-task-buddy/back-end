package com.taskbuddy.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {

    private Long serviceId;
    private String address;
    private LocalDate bookingDate;
}
