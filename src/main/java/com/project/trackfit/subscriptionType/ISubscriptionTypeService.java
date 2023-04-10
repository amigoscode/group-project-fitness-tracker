package com.project.trackfit.subscriptionType;

import java.util.UUID;

public interface ISubscriptionTypeService {
    UUID createSubscriptionType(CreateSubscriptionTypeRequest createSubscriptionTypeRequest);
    RetrieveSubscriptionTypeRequest getSubscriptionTypeById(UUID subscriptionTypeId);


}
