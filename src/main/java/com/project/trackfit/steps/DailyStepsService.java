package com.project.trackfit.steps;

import com.project.trackfit.core.exception.DailyStepsNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DailyStepsService implements IDailyStepsService {

    private final DailyStepsRepository dailyStepsRepository;
    private final DailyStepsRetrieveRequestMapper dailyStepsRetrieveRequestMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public DailyStepsService(DailyStepsRepository dailyStepsRepository,
                             DailyStepsRetrieveRequestMapper dailyStepsRetrieveRequestMapper,
                             CustomerRepository customerRepository) {
        this.dailyStepsRepository = dailyStepsRepository;
        this.dailyStepsRetrieveRequestMapper = dailyStepsRetrieveRequestMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public UUID createDailySteps(DailyStepsRequest dailyStepsRequest) {
        Customer customer = customerRepository.findById(dailyStepsRequest.customerId())
                .orElseThrow(ResourceNotFoundException::new);

        DailySteps dailySteps = new DailySteps();
        dailySteps.setSteps(dailyStepsRequest.steps());
        dailySteps.setDate(dailyStepsRequest.date());
        dailySteps.setCustomer(customer);
        dailyStepsRepository.save(dailySteps);
        return dailySteps.getId();
    }

    @Override
    public DailyStepsResponse retrieveDailyStepsById(UUID dailyStepsId) {
        return dailyStepsRepository
                .findById(dailyStepsId)
                .map(dailyStepsRetrieveRequestMapper)
                .orElseThrow(DailyStepsNotFoundException::new);
    }
}
