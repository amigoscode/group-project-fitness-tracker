package com.project.trackfit.measurements;

import com.project.trackfit.customer.dto.Customer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MeasurementsRetrieveRequestMapper implements Function<Measurements, RetrieveMeasurementsRequest> {
    @Override
    public RetrieveMeasurementsRequest apply(Measurements measurements) {
        Customer customer = measurements.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("User is null for customer with ID " + measurements.getId());
        }
        return new RetrieveMeasurementsRequest(
                measurements.getId(),
                measurements.getHeight(),
                measurements.getWeight(),
                measurements.getDate()
        );
    }
}