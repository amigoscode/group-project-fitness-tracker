package com.project.trackfit.steps;

import com.project.trackfit.customer.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DailyStepsRetrieveRequestMapper implements Function<DailySteps, RetrieveDailyStepsRequest> {
    @Override
    public RetrieveDailyStepsRequest apply(DailySteps steps) {
        Customer customer = steps.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("User is null for customer with ID " + steps.getId());
        }
        return new RetrieveDailyStepsRequest(
                steps.getId(),
                steps.getSteps(),
                steps.getDate()
        );
    }
}