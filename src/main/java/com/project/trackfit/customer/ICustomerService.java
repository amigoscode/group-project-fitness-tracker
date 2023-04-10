package com.project.trackfit.customer;

import com.project.trackfit.core.ApplicationUser;

import java.util.UUID;

public interface ICustomerService {
    UUID createCustomer(ApplicationUser applicationUser, CreateCustomerRequest createCustomerRequest);
    Customer getCustomerById(UUID customer_id);
    RetrieveCustomerRequest RetrieveCustomerById(UUID customer_id);
}