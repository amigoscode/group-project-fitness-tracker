package com.project.trackfit.subscription;

import java.time.LocalDateTime;

import java.util.UUID;

public record CreateSubscriptionRequest(
         UUID personalTrainerId,
         UUID customerId,
         LocalDateTime subscribedAt
) {
}