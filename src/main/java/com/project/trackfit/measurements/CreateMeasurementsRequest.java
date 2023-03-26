package com.project.trackfit.measurements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public record CreateMeasurementsRequest(String height, String weight, String date, UUID customerId, UUID measurementsId) {
    public Date getParsedDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(date);
    }
}