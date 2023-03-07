package com.project.trackfit.subscription;



import java.util.UUID;

public interface SubscriptionService {
    void createSubscription(CreateSubscriptionRequest subscription);
    Subscription findSubscriptionByID(UUID subscription_id);
    Iterable<RetrieveSubscriptionRequest> findAllSubscription();


}
