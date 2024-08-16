package com.project.trackfit.steps;

import java.time.LocalDateTime;
import java.util.UUID;

public record DailyStepsRequest(
        String steps,
        LocalDateTime date,
        UUID customerId,
        UUID dailyStepsId) { }