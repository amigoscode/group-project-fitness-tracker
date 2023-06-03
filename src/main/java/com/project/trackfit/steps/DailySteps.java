package com.project.trackfit.steps;

import com.project.trackfit.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "daily_steps")
public class DailySteps {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Column(nullable = false)
    private String steps;
    @Column(nullable = false)
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}