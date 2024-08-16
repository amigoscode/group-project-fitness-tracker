package com.project.trackfit.subscriptionType;

import java.util.UUID;

public interface ISubscriptionTypeService {
    UUID createSubscriptionType(SubscriptionTypeRequest subscriptionTypeRequest);
    SubscriptionTypeResponse getSubscriptionTypeById(UUID subscriptionTypeId);
}
