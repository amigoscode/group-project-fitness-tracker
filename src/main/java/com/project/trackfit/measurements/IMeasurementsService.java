package com.project.trackfit.measurements;

import java.util.UUID;

public interface IMeasurementsService {
    UUID createMeasurements(CreateMeasurementsRequest createMeasurementsRequest);
    MeasurementsResponse retrieveMeasurementsById(UUID measurementsId);
    void updateCustomerMeasurements(UUID customerId, CreateMeasurementsRequest createMeasurementsRequest);
    void deleteMeasurementById(UUID measurementId);
}