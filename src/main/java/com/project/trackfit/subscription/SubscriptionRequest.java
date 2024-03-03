package com.project.trackfit.subscription;

import java.util.UUID;

public record SubscriptionRequest(
         UUID personalTrainerId,
         UUID customerId,
         UUID subscriptionTypeId
) { }