package com.project.trackfit.subscriptionType;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.trackfit.subscription.Subscription;
import com.project.trackfit.trainer.PersonalTrainer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscription_type")
public class SubscriptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String title;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name="trainer_id")
    private PersonalTrainer personalTrainer;

    @OneToMany(mappedBy = "subscriptionType")
    private List<Subscription> subscriptions;

    private LocalDateTime createdAt;

    private int periodInDays;

    private String description;

    public SubscriptionType() {
    }

    public SubscriptionType(String title,
                            PersonalTrainer personalTrainer,
                            LocalDateTime createdAt,
                            int periodInDays) {
        this.title = title;
        this.personalTrainer = personalTrainer;
        this.createdAt = createdAt;
        this.periodInDays = periodInDays;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }

    public void setPersonalTrainer(PersonalTrainer personalTrainer) {
        this.personalTrainer = personalTrainer;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getPeriodInDays() {
        return periodInDays;
    }

    public void setPeriodInDays(int periodInDays) {
        this.periodInDays = periodInDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
