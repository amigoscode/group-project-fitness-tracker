package com.project.trackfit.subscription;

import com.project.trackfit.customer.dto.CustomerResponse;

import com.project.trackfit.trainer.dto.TrainerResponse;

import java.time.LocalDateTime;

import java.util.UUID;

public record SubscriptionResponse(
         UUID id,
         LocalDateTime subscribedAt,
         LocalDateTime expiredOn,
         Boolean isActive,
         TrainerResponse personalTrainer,
         CustomerResponse customer
        ) { }