package com.project.trackfit.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.subscriptionType.SubscriptionType;
import com.project.trackfit.trainer.PersonalTrainer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name="subscription_type_id",nullable = false)
    private SubscriptionType subscriptionType;
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