package com.project.trackfit.subscription;

import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.dto.CustomerResponse;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.subscriptionType.ISubscriptionTypeService;
import com.project.trackfit.subscriptionType.SubscriptionTypeResponse;
import com.project.trackfit.subscriptionType.SubscriptionType;
import com.project.trackfit.subscriptionType.SubscriptionTypeRepository;
import com.project.trackfit.trainer.entity.PersonalTrainer;
import com.project.trackfit.trainer.dto.TrainerResponse;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.project.trackfit.core.mapper.CommonMapper.mapToCustomerResponse;
import static com.project.trackfit.core.mapper.CommonMapper.mapToSubscriptionResponse;
import static com.project.trackfit.core.mapper.CommonMapper.mapToTrainerResponse;

@Service
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final PersonalTrainerRepository personalTrainerRepository;
    private final CustomerRepository customerRepository;
    private final ISubscriptionTypeService subscriptionTypeService;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               SubscriptionTypeRepository subscriptionTypeRepository,
                               PersonalTrainerRepository personalTrainerRepository,
                               CustomerRepository customerRepository,
                               ISubscriptionTypeService subscriptionTypeService) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
        this.personalTrainerRepository = personalTrainerRepository;
        this.customerRepository = customerRepository;
        this.subscriptionTypeService = subscriptionTypeService;
    }

    @Override
    public UUID createSubscription(SubscriptionRequest subscriptionRequest) {
        PersonalTrainer trainer = personalTrainerRepository
                .findById(subscriptionRequest.personalTrainerId())
                .orElseThrow(ResourceNotFoundException::new);

        Customer currentCustomer = customerRepository
                .findById(subscriptionRequest.customerId())
                .orElseThrow(ResourceNotFoundException::new);

        SubscriptionTypeResponse subscriptionTypeRequest = subscriptionTypeService.getSubscriptionTypeById(subscriptionRequest.subscriptionTypeId());

        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(subscriptionTypeRequest.subscription_id())
                .orElseThrow(ResourceNotFoundException::new);

        Subscription subscribe = new Subscription(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                currentCustomer,
                trainer,
                subscriptionType
        );

        subscriptionRepository.save(subscribe);
        return subscribe.getId();
    }

    @Override
    public SubscriptionResponse findSubscriptionByID(UUID subscription_id) {
        return subscriptionRepository
                .findById(subscription_id)
                .map(this::mapToRetrieveSubscriptionRequest)
                .orElseThrow(ResolutionException::new);
    }

    @Override
    public List<SubscriptionResponse> findAllSubscription() {
        return subscriptionRepository.findAll()
                .stream()
                .map(this::mapToRetrieveSubscriptionRequest)
                .collect(Collectors.toList());
    }

    private SubscriptionResponse mapToRetrieveSubscriptionRequest(Subscription subscription) {
        Customer customer = subscription.getCustomer();
        PersonalTrainer trainer = subscription.getPersonalTrainer();

        CustomerResponse customerRequest = mapToCustomerResponse(customer);
        TrainerResponse trainerRequest = mapToTrainerResponse(trainer);

        return mapToSubscriptionResponse(subscription, customerRequest, trainerRequest);
    }
}