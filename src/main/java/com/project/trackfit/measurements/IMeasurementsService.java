package com.project.trackfit.measurements;

import java.util.List;
import java.util.UUID;

public interface IMeasurementsService {
    UUID createMeasurements(CreateMeasurementsRequest createMeasurementsRequest);
    List<RetrieveMeasurementsRequest> getCustomerMeasurements(UUID customerId);
    RetrieveMeasurementsRequest retrieveMeasurementsById(UUID measurementsId);
    void updateCustomerMeasurements(UUID customerId, CreateMeasurementsRequest createMeasurementsRequest);
    void deleteMeasurementById(UUID measurementId);
}