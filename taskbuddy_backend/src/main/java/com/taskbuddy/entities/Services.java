package com.taskbuddy.entities;
import java.math.BigDecimal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "services")
@AttributeOverride(name = "id", column = @Column(name = "service_id"))
public class Services extends BaseEntity {

    

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
    private ServiceName name;

    private String description;
    private BigDecimal basePrice;
}
