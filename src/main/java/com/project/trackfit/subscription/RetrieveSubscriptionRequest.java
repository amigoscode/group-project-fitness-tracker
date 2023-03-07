package com.project.trackfit.subscription;

import com.project.trackfit.customer.RetrieveCustomerRequest;

import com.project.trackfit.trainer.RetrieveTrainerRequest;

import java.time.LocalDateTime;

import java.util.UUID;

public record RetrieveSubscriptionRequest(
         UUID id,
         LocalDateTime subscribedAt,
         LocalDateTime expiredOn,
         Boolean isActive,
         RetrieveTrainerRequest personalTrainer,
         RetrieveCustomerRequest customer


        ) {
}
