package com.project.trackfit.subscriptionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSubscriptionTypeRequest(
        UUID trainer_id,
        UUID subscription_id,
        String subscription_type_title,
        LocalDateTime created_at,
        Integer period_in_days

) {
}
