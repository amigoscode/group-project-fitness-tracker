package com.project.trackfit.subscription;

import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerService;
import com.project.trackfit.trainer.PersonalTrainer;
import com.project.trackfit.trainer.PersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionServiceImplementation implements SubscriptionService {
    private  final SubscriptionRepository subscriptionRepository;
    private  final PersonalTrainerService personalTrainerService;
    private  final CustomerService customerService;
    private  final SubscriptionRetrieveRequestMapper retrieveRequestMapper;


    private Subscription findOrThrow(final UUID subId){
        return subscriptionRepository
                .findById(subId)
                .orElseThrow(ResolutionException::new);
    }
    @Override
    public void createSubscription(CreateSubscriptionRequest subscriptionRequest) {

        PersonalTrainer trainer = personalTrainerService.getTrainerByID(subscriptionRequest.personalTrainerId());
        Customer currentCustomer=customerService.getCustomerById(subscriptionRequest.customerId());

        //Subscribe

        Subscription subscribe= new Subscription(
                subscriptionRequest.subscribedAt(),
            subscriptionRequest.subscribedAt().plus(30, ChronoUnit.DAYS ),
                currentCustomer,
                trainer
        );

        subscriptionRepository.save(subscribe);
    }

    @Override
    public Subscription findSubscriptionByID(UUID subscription_id) {
       return findOrThrow(subscription_id);

    }

    @Override
    public Iterable<RetrieveSubscriptionRequest> findAllSubscription() {
        return subscriptionRepository.findAll()
                .stream()
                .map(retrieveRequestMapper)
                .collect(Collectors.toList());
    }
}
