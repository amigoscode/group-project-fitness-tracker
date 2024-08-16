package com.project.trackfit.customer;

import com.project.trackfit.user.Role;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String address,
        Role role,
        String phoneNumber
) { }