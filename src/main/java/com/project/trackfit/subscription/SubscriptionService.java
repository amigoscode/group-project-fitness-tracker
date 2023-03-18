package com.project.trackfit.subscription;

import java.util.UUID;

public interface SubscriptionService {
    UUID createSubscription(CreateSubscriptionRequest subscription);
    RetrieveSubscriptionRequest findSubscriptionByID(UUID subscription_id);
    Iterable<RetrieveSubscriptionRequest> findAllSubscription();
}
