package com.project.trackfit.customer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.trackfit.user.User;
import com.project.trackfit.measurements.Measurements;
import com.project.trackfit.steps.DailySteps;
import com.project.trackfit.subscription.Subscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy ="customer")
    private Set<Measurements> measurements;

    @JsonManagedReference
    @OneToMany(mappedBy ="customer")
    private Set<Subscription> subscriptions;

    @JsonManagedReference
    @OneToMany(mappedBy ="customer")
    private Set<DailySteps> steps;

    @JsonManagedReference
    @OneToMany(mappedBy ="customer")
    private Set<Media> media;

    public Customer() {
    }

    public Customer(User user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Measurements> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Set<Measurements> measurements) {
        this.measurements = measurements;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<DailySteps> getSteps() {
        return steps;
    }

    public void setSteps(Set<DailySteps> steps) {
        this.steps = steps;
    }

    public Set<Media> getMedia() {
        return media;
    }

    public void setMedia(Set<Media> media) {
        this.media = media;
    }
}
