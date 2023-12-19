package com.project.trackfit.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.subscriptionType.SubscriptionType;
import com.project.trackfit.trainer.PersonalTrainer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import java.util.UUID;

@Entity
@Table(name = "subscription")
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

    public Subscription() {
    }

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(LocalDateTime subscribedAt) {
        this.subscribedAt = subscribedAt;
    }

    public LocalDateTime getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(LocalDateTime expiredOn) {
        this.expiredOn = expiredOn;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }

    public void setPersonalTrainer(PersonalTrainer personalTrainer) {
        this.personalTrainer = personalTrainer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}