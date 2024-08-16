package com.project.trackfit.measurements;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeasurementsResponse(
        UUID id,
        String height,
        String weight,
        LocalDateTime date
        ) { }