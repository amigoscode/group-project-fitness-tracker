package com.project.trackfit.measurements;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetrieveMeasurementsRequest(
        UUID id,
        String height,
        String weight,
        LocalDateTime date
        ) { }