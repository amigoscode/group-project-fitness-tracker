package com.project.trackfit.subscription;

import com.project.trackfit.customer.CustomerRetrieveRequestMapper;
import com.project.trackfit.customer.RetrieveCustomerRequest;
import com.project.trackfit.trainer.dto.RetrieveTrainerRequest;
import com.project.trackfit.trainer.TrainerRetrieveRequestMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class SubscriptionRetrieveRequestMapper implements Function<Subscription, RetrieveSubscriptionRequest> {

    private final TrainerRetrieveRequestMapper trainerRetrieveRequestMapper;
    private final CustomerRetrieveRequestMapper customerRetrieveRequestMapper;

    @Override
    public RetrieveSubscriptionRequest apply(Subscription subscription) {
        RetrieveTrainerRequest trainerRequest = trainerRetrieveRequestMapper.apply(subscription.getPersonalTrainer());
        RetrieveCustomerRequest customerRequest = customerRetrieveRequestMapper.apply(subscription.getCustomer());
        return new RetrieveSubscriptionRequest(
                subscription.getId(),
                subscription.getSubscribedAt(),
                subscription.getExpiredOn(),
                subscription.getActive(),
                trainerRequest,
                customerRequest
        );
    }
}