package com.project.trackfit.trainer;

import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.subscription.Subscription;
import jakarta.persistence.*;
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
    @OneToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isActivated;

    @OneToMany(mappedBy = "personalTrainer")
    private Set<Subscription> subscribers;
    private boolean isSuspended;
    private boolean isExpired;

    public PersonalTrainer(ApplicationUser applicationUser) {
        this.user = applicationUser;
    }
}