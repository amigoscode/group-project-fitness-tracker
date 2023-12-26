package com.project.trackfit.trainer.dto;

import java.util.UUID;

public record RetrieveTrainerRequest(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) { }