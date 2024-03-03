package com.project.trackfit.core.mapper;

import com.project.trackfit.customer.CustomerResponse;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.subscription.Subscription;
import com.project.trackfit.subscription.SubscriptionResponse;
import com.project.trackfit.trainer.PersonalTrainerResponse;
import com.project.trackfit.trainer.PersonalTrainer;

public class CommonMapper {

    public static CustomerResponse mapToCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getUser().getFirstName(),
                customer.getUser().getLastName(),
                customer.getUser().getAge(),
                customer.getUser().getEmail(),
                customer.getUser().getAddress(),
                customer.getUser().getRole(),
                customer.getUser().getPhoneNumber()
        );
    }

    public static PersonalTrainerResponse mapToTrainerResponse(PersonalTrainer trainer) {
        return new PersonalTrainerResponse(
                trainer.getId(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getAge(),
                trainer.getUser().getEmail(),
                trainer.getUser().getAddress(),
                trainer.getUser().getRole(),
                trainer.getUser().getPhoneNumber()
        );
    }

    public static SubscriptionResponse mapToSubscriptionResponse(
            Subscription subscription,
            CustomerResponse customerResponse,
            PersonalTrainerResponse personalTrainerResponse) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getSubscribedAt(),
                subscription.getExpiredOn(),
                subscription.getActive(),
                personalTrainerResponse,
                customerResponse
        );
    }
}
