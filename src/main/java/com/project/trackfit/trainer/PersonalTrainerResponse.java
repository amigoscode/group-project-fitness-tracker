package com.project.trackfit.trainer;

import com.project.trackfit.user.Role;

import java.util.UUID;

public record PersonalTrainerResponse(
        UUID id,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String address,
        Role role,
        String phoneNumber
) { }