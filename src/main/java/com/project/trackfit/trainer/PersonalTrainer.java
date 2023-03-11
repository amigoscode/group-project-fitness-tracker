package com.project.trackfit.trainer;

import com.project.trackfit.subscription.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PersonalTrainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @NotNull(message = "Email is required")
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isActivated;

    @OneToMany(mappedBy = "personalTrainer")
    private Set<Subscription> subscribers;
    private boolean isSuspended;
    private boolean isExpired;

    public PersonalTrainer(String email, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}