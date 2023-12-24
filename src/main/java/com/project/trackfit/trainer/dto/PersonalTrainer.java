package com.project.trackfit.trainer.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.trackfit.user.dto.ApplicationUser;
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
@Table(name = "personal_trainer")
public class PersonalTrainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private boolean isActivated;

    @JsonManagedReference
    @OneToMany(mappedBy = "personalTrainer")
    private Set<Subscription> subscribers;

    private boolean isSuspended;

    private boolean isExpired;

    public PersonalTrainer() {
    }

    public PersonalTrainer(UUID id,
                           ApplicationUser user,
                           String firstName,
                           String lastName,
                           String phoneNumber,
                           boolean isActivated,
                           Set<Subscription> subscribers,
                           boolean isSuspended,
                           boolean isExpired) {
        this.id = id;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isActivated = isActivated;
        this.subscribers = subscribers;
        this.isSuspended = isSuspended;
        this.isExpired = isExpired;
    }

    public PersonalTrainer(ApplicationUser applicationUser) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public Set<Subscription> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Subscription> subscribers) {
        this.subscribers = subscribers;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}