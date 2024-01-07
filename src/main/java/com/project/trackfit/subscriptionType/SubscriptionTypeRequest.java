package com.project.trackfit.subscriptionType;

import java.util.UUID;

public record SubscriptionTypeRequest(
        UUID trainer_id,
        String subscription_type_title,
        Integer period_in_days
) { }
