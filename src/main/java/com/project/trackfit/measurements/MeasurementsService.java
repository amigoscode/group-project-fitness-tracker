package com.project.trackfit.measurements;

import com.project.trackfit.core.exception.MeasurementNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MeasurementsService implements IMeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UUID createMeasurements(CreateMeasurementsRequest createMeasurementsRequest) {
        Customer customer = customerRepository.findById(createMeasurementsRequest.customerId())
                .orElseThrow(ResourceNotFoundException::new);

        Measurements measurements = new Measurements();
        measurements.setHeight(createMeasurementsRequest.height());
        measurements.setWeight(createMeasurementsRequest.weight());
        measurements.setDate(LocalDateTime.now());
        measurements.setCustomer(customer);
        measurementsRepository.save(measurements);
        return measurements.getId();
    }

    @Override
    public MeasurementsResponse retrieveMeasurementsById(UUID measurementsId) {
        Measurements measurements = measurementsRepository
                .findById(measurementsId)
                .orElseThrow(MeasurementNotFoundException::new);

        return new MeasurementsResponse(
                measurements.getId(),
                measurements.getHeight(),
                measurements.getWeight(),
                measurements.getDate()
        );
    }

    @Override
    public void updateCustomerMeasurements(UUID customerId, CreateMeasurementsRequest createMeasurementsRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        Measurements measurements;
        if (createMeasurementsRequest.measurementsId() != null) {
            measurements = measurementsRepository.findById(createMeasurementsRequest.measurementsId())
                    .orElseThrow(MeasurementNotFoundException::new);
        } else {
            measurements = new Measurements();
            measurements.setCustomer(customer);
        }

        measurements.setHeight(createMeasurementsRequest.height());
        measurements.setWeight(createMeasurementsRequest.weight());
        measurements.setDate(LocalDateTime.now());

        measurementsRepository.save(measurements);
    }

    @Override
    public void deleteMeasurementById(UUID measurementId) {
        Measurements measurements = measurementsRepository
                .findById(measurementId)
                .orElseThrow(MeasurementNotFoundException::new);
        measurementsRepository.delete(measurements);
    }
}