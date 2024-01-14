package com.project.trackfit.trainer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.trackfit.user.entity.ApplicationUser;
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

    private boolean isActivated;

    @JsonManagedReference
    @OneToMany(mappedBy = "personalTrainer")
    private Set<Subscription> subscribers;

    private boolean isSuspended;

    private boolean isExpired;

    public PersonalTrainer() {
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