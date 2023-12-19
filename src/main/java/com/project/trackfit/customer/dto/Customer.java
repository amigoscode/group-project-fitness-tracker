package com.project.trackfit.customer.dto;

import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.measurements.Measurements;
import com.project.trackfit.media.Media;
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
    private ApplicationUser user;

    private int age;

    private String address;

    @OneToMany(mappedBy ="customer")
    private Set<Measurements> measurements;

    @OneToMany(mappedBy ="customer")
    private Set<Subscription> subscriptions;

    @OneToMany(mappedBy ="customer")
    private Set<DailySteps> steps;

    @OneToMany(mappedBy ="customer")
    private Set<Media> media;

    public Customer() {
    }

    public Customer(UUID id,
                    ApplicationUser user,
                    int age,
                    String address,
                    Set<Measurements> measurements,
                    Set<Subscription> subscriptions,
                    Set<DailySteps> steps,
                    Set<Media> media) {
        this.id = id;
        this.user = user;
        this.age = age;
        this.address = address;
        this.measurements = measurements;
        this.subscriptions = subscriptions;
        this.steps = steps;
        this.media = media;
    }

    public Customer(ApplicationUser applicationUser) {
        this.user = applicationUser;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
