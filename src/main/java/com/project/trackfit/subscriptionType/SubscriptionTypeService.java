package com.project.trackfit.subscriptionType;

import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.trainer.entity.PersonalTrainer;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SubscriptionTypeService implements ISubscriptionTypeService {

    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final PersonalTrainerRepository personalTrainerRepository;

    @Autowired
    public SubscriptionTypeService(SubscriptionTypeRepository subscriptionTypeRepository,
                                   PersonalTrainerRepository personalTrainerRepository) {
        this.subscriptionTypeRepository = subscriptionTypeRepository;
        this.personalTrainerRepository = personalTrainerRepository;
    }

    @Override
    public UUID createSubscriptionType(SubscriptionTypeRequest subscriptionTypeRequest) {
        PersonalTrainer trainerInstance = personalTrainerRepository
                .findById(subscriptionTypeRequest.trainer_id())
                .orElseThrow(ResourceNotFoundException::new);

        SubscriptionType subscriptionType = new SubscriptionType(
                subscriptionTypeRequest.subscription_type_title(),
                trainerInstance,
                LocalDateTime.now(),
                subscriptionTypeRequest.period_in_days());
        subscriptionTypeRepository.save(subscriptionType);
        return subscriptionType.getId();
    }

    @Override
    public SubscriptionTypeResponse getSubscriptionTypeById(UUID subscriptionTypeId) {
        return subscriptionTypeRepository
                .findById(subscriptionTypeId)
                .map(this::convertToRetrieveSubscriptionTypeRequest)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private SubscriptionTypeResponse convertToRetrieveSubscriptionTypeRequest(SubscriptionType subscriptionType) {
        return new SubscriptionTypeResponse(
                subscriptionType.getId(),
                subscriptionType.getPersonalTrainer().getUser().getFirstName(),
                subscriptionType.getCreatedAt(),
                subscriptionType.getPeriodInDays(),
                subscriptionType.getTitle()
        );
    }
}
