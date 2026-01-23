package com.taskbuddy.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponse {

    private Long bookingId;
    private String serviceName;
    private String status;
    private double price;
    private LocalDate bookingDate;
}
