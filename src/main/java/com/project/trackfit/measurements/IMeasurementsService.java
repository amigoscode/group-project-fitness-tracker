package com.project.trackfit.measurements;

import java.util.UUID;

public interface IMeasurementsService {
    UUID createMeasurements(MeasurementsRequest measurementsRequest);
    MeasurementsResponse retrieveMeasurementsById(UUID measurementsId);
    void updateCustomerMeasurements(UUID customerId, MeasurementsRequest measurementsRequest);
    void deleteMeasurementById(UUID measurementId);
}