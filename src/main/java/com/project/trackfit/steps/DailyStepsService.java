package com.project.trackfit.steps;

import com.project.trackfit.core.exception.DailyStepsNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DailyStepsService implements IDailyStepsService {

    private final DailyStepsRepository dailyStepsRepository;
    private final DailyStepsRetrieveRequestMapper dailyStepsRetrieveRequestMapper;
    private final CustomerRepository customerRepository;

    @Override
    public UUID createDailySteps(CreateDailyStepsRequest createDailyStepsRequest) {
        Customer customer = customerRepository.findById(createDailyStepsRequest.customerId())
                .orElseThrow(ResourceNotFoundException::new);

        DailySteps dailySteps = new DailySteps();
        dailySteps.setSteps(createDailyStepsRequest.steps());
        dailySteps.setDate(createDailyStepsRequest.date());
        dailySteps.setCustomer(customer);
        dailyStepsRepository.save(dailySteps);
        return dailySteps.getId();
    }

    @Override
    public List<RetrieveDailyStepsRequest> getCustomerDailySteps(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        Set<DailySteps> dailyStepsSet = customer.getSteps();

        return dailyStepsSet.stream()
                .map(dailyStepsRetrieveRequestMapper)
                .collect(Collectors.toList());
    }

    @Override
    public RetrieveDailyStepsRequest retrieveDailyStepsById(UUID dailyStepsId) {
        return dailyStepsRepository
                .findById(dailyStepsId)
                .map(dailyStepsRetrieveRequestMapper)
                .orElseThrow(DailyStepsNotFoundException::new);
    }
}
