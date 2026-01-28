package com.taskbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int experienceInYears;
    private int fees;
    private double rating;
    private boolean availablity;
    private AddressDTO address;
    private List services;
    private Boolean verified = false; 
}