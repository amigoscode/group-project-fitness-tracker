package com.project.trackfit.measurements;

import java.util.Date;
import java.util.UUID;

public record RetrieveMeasurementsRequest(
        UUID id,
        String height,
        String weight,
        Date date
        ) { }