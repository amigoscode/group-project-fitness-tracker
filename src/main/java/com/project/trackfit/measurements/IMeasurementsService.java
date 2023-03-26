package com.project.trackfit.measurements;

import com.project.trackfit.customer.Customer;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface IMeasurementsService {
    UUID createMeasurements(CreateMeasurementsRequest createMeasurementsRequest) throws ParseException;
    List<RetrieveMeasurementsRequest> getCustomerMeasurements(UUID customerId);
    RetrieveMeasurementsRequest retrieveMeasurementsById(UUID measurementsId);
    void updateCustomerMeasurements(UUID customerId, CreateMeasurementsRequest createMeasurementsRequest) throws ParseException;
    void deleteMeasurementById(UUID measurementId);
}