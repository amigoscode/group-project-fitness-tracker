package com.project.trackfit.measurements;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateMeasurementsRequest(
        String height,
        String weight,
        LocalDateTime date,
        UUID customerId,
        UUID measurementsId) { }