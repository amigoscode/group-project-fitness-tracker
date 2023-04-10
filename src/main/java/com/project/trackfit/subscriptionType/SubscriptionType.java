package com.project.trackfit.subscriptionType;

import com.project.trackfit.customer.Customer;
import com.project.trackfit.trainer.PersonalTrainer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubscriptionType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name="trainer_id")
    private PersonalTrainer personalTrainer;

    @OneToMany(mappedBy = "customer")
    private List<Customer> customers;

    private LocalDateTime createdAt;

    private int periodInDays;

    private String description;


}
