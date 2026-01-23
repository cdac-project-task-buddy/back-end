package com.taskbuddy.dto;

import lombok.Data;

@Data
public class ServiceRequest {
    private String name;
    private String description;
    private double basePrice;
}
