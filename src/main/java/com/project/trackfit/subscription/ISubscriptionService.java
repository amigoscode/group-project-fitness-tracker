package com.project.trackfit.subscription;

import java.util.List;
import java.util.UUID;

public interface ISubscriptionService {
    UUID createSubscription(SubscriptionRequest subscription);
    SubscriptionResponse findSubscriptionByID(UUID subscription_id);
    List<SubscriptionResponse> findAllSubscription();
}