package com.project.trackfit.subscription;

import java.util.UUID;

public interface ISubscriptionService {
    UUID createSubscription(CreateSubscriptionRequest subscription);
    RetrieveSubscriptionRequest findSubscriptionByID(UUID subscription_id);
    Iterable<RetrieveSubscriptionRequest> findAllSubscription();
}