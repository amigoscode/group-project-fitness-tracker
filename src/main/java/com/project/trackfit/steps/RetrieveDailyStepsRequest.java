package com.project.trackfit.steps;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetrieveDailyStepsRequest(
        UUID id,
        String steps,
        LocalDateTime date
        ) { }