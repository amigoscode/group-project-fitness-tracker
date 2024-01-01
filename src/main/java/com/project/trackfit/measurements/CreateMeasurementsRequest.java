package com.project.trackfit.measurements;

import java.util.UUID;

public record CreateMeasurementsRequest(
        String height,
        String weight,
        UUID customerId,
        UUID measurementsId) { }