package com.project.trackfit.customer.dto;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String address
) { }