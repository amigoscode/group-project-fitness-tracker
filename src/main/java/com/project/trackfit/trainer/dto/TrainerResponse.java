package com.project.trackfit.trainer.dto;

import com.project.trackfit.user.component.Role;

import java.util.UUID;

public record TrainerResponse(
        UUID id,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String address,
        Role role,
        String phoneNumber
) { }