package com.project.trackfit.steps;

import java.time.LocalDateTime;
import java.util.UUID;

public record DailyStepsResponse(
        UUID id,
        String steps,
        LocalDateTime date
        ) { }