package com.taskbuddy.entities;

import java.time.LocalTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//JPA annotations
@Entity
@Table(name = "availability")
//Lombok annotations
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Availability extends BaseEntity {
	private LocalTime startTime;
    private LocalTime endTime;
    private String day; 
    
    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;
}
