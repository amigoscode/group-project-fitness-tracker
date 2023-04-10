package com.project.trackfit.subscriptionType;

import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.subscription.RetrieveSubscriptionRequest;
import com.project.trackfit.subscription.Subscription;
import com.project.trackfit.subscription.SubscriptionRepository;
import com.project.trackfit.subscription.SubscriptionService;
import com.project.trackfit.trainer.PersonalTrainer;
import com.project.trackfit.trainer.PersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionTypeService implements ISubscriptionTypeService{
    private  final SubscriptionTypeRepository subscriptionTypeRepository;
    private  final SubscriptionRepository subscriptionRepository;
    private  final PersonalTrainerService personalTrainerService;
    @Override
    public UUID createSubscriptionType(CreateSubscriptionTypeRequest createSubscriptionTypeRequest) {
        PersonalTrainer trainerInstance=personalTrainerService.
                getTrainerByID(createSubscriptionTypeRequest.trainer_id());
        Subscription subscriptionInstance=subscriptionRepository
                .findById(createSubscriptionTypeRequest.subscription_id())
                .orElseThrow(ResourceNotFoundException::new);

        SubscriptionType subscriptionType=
                new SubscriptionType(

                );

        return null;
    }

    @Override
    public RetrieveSubscriptionTypeRequest getSubscriptionTypeById(UUID subscriptionTypeId) {
        return null;
    }
}
