package com.project.trackfit.personalTrainer.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PersonalTrainerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;
    @NotNull(message = "Email is required")
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String phoneNumber;
    private  boolean isActivated;
    private boolean isSuspended;
    private boolean isExpired;





}
