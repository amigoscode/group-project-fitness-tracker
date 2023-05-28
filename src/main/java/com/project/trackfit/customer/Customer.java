package com.project.trackfit.customer;

import com.project.trackfit.core.ApplicationUser;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    public Customer(ApplicationUser applicationUser) {
        this.user = applicationUser;
    }
}
