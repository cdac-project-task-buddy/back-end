package com.taskbuddy.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity

@Table(name = "bookings",
uniqueConstraints = @UniqueConstraint(columnNames = {"booking_date_time","worker_id"}))
@AttributeOverride(name = "id", column = @Column(name = "booking_id"))
//Lombok annotations
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class Booking extends BaseEntity {
	@Column(name="booking_date_time")
	private LocalDateTime bookingDate;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	private BigDecimal price;
	
	// *----->1 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="address_id",nullable = false)
	private Address myAddress;
	// *----->1 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service_id",nullable = false)
	private Services myService;
	// *----->1 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="worker_id",nullable = false)
	private Worker myWorker;
	// *----->1 
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name="customer_id",nullable = false)
	private Customer myCustomer;
	
	@OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Review review;

	

}
    
