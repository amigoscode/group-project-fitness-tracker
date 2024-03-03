package com.project.trackfit.subscription;

import com.project.trackfit.customer.CustomerResponse;

import com.project.trackfit.trainer.PersonalTrainerResponse;

import java.time.LocalDateTime;

import java.util.UUID;

public record SubscriptionResponse(
         UUID id,
         LocalDateTime subscribedAt,
         LocalDateTime expiredOn,
         Boolean isActive,
         PersonalTrainerResponse personalTrainer,
         CustomerResponse customer
        ) { }