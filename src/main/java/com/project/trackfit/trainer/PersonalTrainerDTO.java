package com.project.trackfit.trainer;

import com.project.trackfit.subscription.Subscription;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
@Getter
@Setter
public class PersonalTrainerDTO {

    private UUID id;
    @NotNull(message = "Email is required")
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String phoneNumber;
    private  boolean isActivated;
    private Set<Subscription> subscribers;




}
