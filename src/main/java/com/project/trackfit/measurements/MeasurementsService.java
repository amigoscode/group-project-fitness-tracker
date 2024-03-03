package com.project.trackfit.measurements;

import com.project.trackfit.core.exception.MeasurementNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MeasurementsService implements IMeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository,
                               CustomerRepository customerRepository) {
        this.measurementsRepository = measurementsRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public UUID createMeasurements(MeasurementsRequest measurementsRequest) {
        Customer customer = customerRepository.findById(measurementsRequest.customerId())
                .orElseThrow(ResourceNotFoundException::new);

        Measurements measurements = new Measurements();
        measurements.setHeight(measurementsRequest.height());
        measurements.setWeight(measurementsRequest.weight());
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
    public void updateCustomerMeasurements(UUID customerId, MeasurementsRequest measurementsRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        Measurements measurements;
        if (measurementsRequest.measurementsId() != null) {
            measurements = measurementsRepository.findById(measurementsRequest.measurementsId())
                    .orElseThrow(MeasurementNotFoundException::new);
        } else {
            measurements = new Measurements();
            measurements.setCustomer(customer);
        }

        measurements.setHeight(measurementsRequest.height());
        measurements.setWeight(measurementsRequest.weight());
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