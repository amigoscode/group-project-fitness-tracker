package com.project.trackfit.measurements;

import com.project.trackfit.core.exception.MeasurementNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MeasurementsService implements IMeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final MeasurementsRetrieveRequestMapper measurementsRetrieveRequestMapper;
    private final CustomerRepository customerRepository;

    @Override
    public UUID createMeasurements(CreateMeasurementsRequest createMeasurementsRequest) throws ParseException {
        Customer customer = customerRepository.findById(createMeasurementsRequest.customerId())
                .orElseThrow(ResourceNotFoundException::new);

        Measurements measurements = new Measurements();
        measurements.setHeight(createMeasurementsRequest.height());
        measurements.setWeight(createMeasurementsRequest.weight());
        measurements.setDate(createMeasurementsRequest.getParsedDate());
        measurements.setCustomer(customer);
        measurementsRepository.save(measurements);
        return measurements.getId();
    }

    @Override
    public List<RetrieveMeasurementsRequest> getCustomerMeasurements(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        Set<Measurements> measurementsSet = customer.getMeasurements();

        return measurementsSet.stream()
                .map(measurementsRetrieveRequestMapper)
                .collect(Collectors.toList());
    }

    @Override
    public RetrieveMeasurementsRequest retrieveMeasurementsById(UUID measurementsId) {
        return measurementsRepository
                .findById(measurementsId)
                .map(measurementsRetrieveRequestMapper)
                .orElseThrow(MeasurementNotFoundException::new);
    }

    @Override
    public void updateCustomerMeasurements(UUID customerId, CreateMeasurementsRequest createMeasurementsRequest) throws ParseException {
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
        measurements.setDate(createMeasurementsRequest.getParsedDate());

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