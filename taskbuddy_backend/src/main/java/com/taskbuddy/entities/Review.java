package com.taskbuddy.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@AttributeOverride(name = "id", column = @Column(name = "reviews_id"))
@Getter
@Setter
@NoArgsConstructor
public class Review extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    private Integer rating;
    private String comment;
}