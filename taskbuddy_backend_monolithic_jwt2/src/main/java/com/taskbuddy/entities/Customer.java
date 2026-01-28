package com.taskbuddy.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//JPA 
@Entity
@Table(name = "customers")
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
//Lombok
@NoArgsConstructor
@Getter
@Setter

@ToString(exclude = {"userDetails"})
public class Customer extends BaseEntity {
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;
	
	@OneToOne(cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",nullable = false)
	private User userDetails;
	  

    @OneToMany(mappedBy = "myCustomer", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();
	

}
