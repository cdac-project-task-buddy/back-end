package com.taskbuddy.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "addresses")
@AttributeOverride(name = "id", column = @Column(name = "address_id"))
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {

	@Column(nullable = false)
    private String street;

    private String area;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @NotBlank
    @Column(nullable = false, length = 6)
    private String pincode;

    private String country = "India";
    
   

    
    public Address(String street, String area, String city, String state, String pincode) {
        this.street = street;
        this.area = area;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

}

