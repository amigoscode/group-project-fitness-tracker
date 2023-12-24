package com.project.trackfit.subscriptionType;

import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.trainer.service.PersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionTypeService implements ISubscriptionTypeService {

    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final PersonalTrainerService personalTrainerService;
    private final RetrieveSubscriptionTypeMapper retrieveSubscriptionTypeMapper;

    private SubscriptionType findOrThrow(UUID subscriptionTypeId){
        return subscriptionTypeRepository
                .findById(subscriptionTypeId)
                .orElseThrow(ResourceNotFoundException::new);
    }
    @Override
    public UUID createSubscriptionType(CreateSubscriptionTypeRequest createSubscriptionTypeRequest) {
        PersonalTrainer trainerInstance = personalTrainerService.
                getTrainerByID(createSubscriptionTypeRequest.trainer_id());

        SubscriptionType subscriptionType=
                new SubscriptionType(
                        createSubscriptionTypeRequest.subscription_type_title(),
                        trainerInstance,
                        createSubscriptionTypeRequest.created_at(),
                        createSubscriptionTypeRequest.period_in_days()
                );
        subscriptionTypeRepository.save(subscriptionType);
        return subscriptionType.getId();
    }

    @Override
    public RetrieveSubscriptionTypeRequest getSubscriptionTypeById(UUID subscriptionTypeId) {
        SubscriptionType instance= findOrThrow(subscriptionTypeId);
        return retrieveSubscriptionTypeMapper.apply(instance);
    }
}
