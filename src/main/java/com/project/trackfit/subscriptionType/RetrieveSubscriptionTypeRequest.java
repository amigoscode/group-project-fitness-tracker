package com.project.trackfit.subscriptionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetrieveSubscriptionTypeRequest(
        UUID id,
        String trainer_name,
        LocalDateTime created_at,
        Integer period_in_days,
        String subscription_type_title
) {
}
