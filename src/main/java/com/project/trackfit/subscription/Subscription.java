package com.project.trackfit.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.trainer.PersonalTrainer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @NotNull(message = "subscribed_on is required")
    @JsonIgnore
    private LocalDateTime subscribedAt;
    private LocalDateTime expiredOn;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "trainer_id",nullable = false)
    private PersonalTrainer personalTrainer;

    @ManyToOne
    @JoinColumn(name="customer_id",nullable = false)
    private Customer customer;

    public Subscription(
            LocalDateTime subscribedAt,
            LocalDateTime expiredOn,
            Customer customer,
            PersonalTrainer trainer
    ) {
        this.subscribedAt=subscribedAt;
        this.expiredOn=expiredOn;
        this.customer=customer;
        this.personalTrainer=trainer;
    }
}
