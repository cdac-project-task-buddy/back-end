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
public class AddressDTO {
    private Long id;
    private String street;
    private String area;
    private String city;
    private String state;
    private String pincode;
    private String country;
}