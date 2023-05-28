package com.project.trackfit.subscriptionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class RetrieveSubscriptionTypeMapper implements Function<SubscriptionType,RetrieveSubscriptionTypeRequest> {
    @Override
    public RetrieveSubscriptionTypeRequest apply(SubscriptionType subscriptionType) {

        return new RetrieveSubscriptionTypeRequest(
                subscriptionType.getId(),
              subscriptionType.getPersonalTrainer().getFirstName()
                      +""
                      +subscriptionType.getPersonalTrainer().getLastName(),
                subscriptionType.getCreatedAt(),
                subscriptionType.getPeriodInDays(),
                subscriptionType.getTitle()
        );
    }
}
