package com.taskbuddy.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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


@Entity
@Table(name="workers")
@AttributeOverride(name="id",column = @Column(name="worker_id"))
//Lombok annotations
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true,exclude = {"userDetails"})
public class Worker extends BaseEntity {
	@Column(name="experience_in_years")
	private int experienceInYears;
	private int fees;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	  name = "worker_services",
	  joinColumns = @JoinColumn(name = "worker_id"),
	  inverseJoinColumns = @JoinColumn(name = "service_id")
	)
	private List<Services> services;

	private Boolean verified = false; // Default to false
	
	private double rating;
	
	private boolean availablity = true;
	
	@OneToOne //mandatory
	@JoinColumn(name="user_id",nullable = false)
	private User userDetails;
	
	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "address_id")
	 private Address address;
	

	@OneToMany(mappedBy = "myWorker", cascade = CascadeType.ALL/* ,fetch=FetchType.EAGER */,orphanRemoval = true)
	@JsonIgnore
	private List<Booking> Bookings=new ArrayList<>();
}
