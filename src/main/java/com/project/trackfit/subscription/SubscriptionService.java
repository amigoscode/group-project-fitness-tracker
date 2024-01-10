package com.project.trackfit.subscription;

import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.dto.CustomerResponse;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.service.ICustomerService;
import com.project.trackfit.subscriptionType.ISubscriptionTypeService;
import com.project.trackfit.subscriptionType.SubscriptionTypeResponse;
import com.project.trackfit.subscriptionType.SubscriptionType;
import com.project.trackfit.subscriptionType.SubscriptionTypeRepository;
import com.project.trackfit.trainer.entity.PersonalTrainer;
import com.project.trackfit.trainer.dto.TrainerResponse;
import com.project.trackfit.trainer.service.IPersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final IPersonalTrainerService personalTrainerService;
    private final ICustomerService customerService;
    private final ISubscriptionTypeService subscriptionTypeService;

    @Override
    public UUID createSubscription(SubscriptionRequest subscriptionRequest) {
        PersonalTrainer trainer = personalTrainerService.getTrainerByID(subscriptionRequest.personalTrainerId());
        Customer currentCustomer = customerService.getCustomerById(subscriptionRequest.customerId());

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

        CustomerResponse customerRequest = new CustomerResponse(
                customer.getId(),
                customer.getUser().getFirstName(),
                customer.getUser().getLastName(),
                customer.getAge(),
                customer.getUser().getEmail(),
                customer.getAddress()
        );

        TrainerResponse trainerRequest = new TrainerResponse(
                trainer.getId(),
                trainer.getUser().getEmail(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getPhoneNumber()
        );

        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getSubscribedAt(),
                subscription.getExpiredOn(),
                subscription.getActive(),
                trainerRequest,
                customerRequest
        );
    }
}