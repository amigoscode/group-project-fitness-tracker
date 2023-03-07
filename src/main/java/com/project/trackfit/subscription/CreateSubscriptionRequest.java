package com.project.trackfit.subscription;

import com.project.trackfit.customer.Customer;
import com.project.trackfit.trainer.PersonalTrainer;

import java.time.LocalDateTime;


import java.util.UUID;

public record CreateSubscriptionRequest(

         UUID personalTrainerId,
         Long customerId,
         LocalDateTime subscribedAt



) {
}
