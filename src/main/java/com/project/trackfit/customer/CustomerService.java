package com.project.trackfit.customer;

import com.project.trackfit.core.ApplicationUser;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface CustomerService {
    UUID createCustomer(ApplicationUser applicationUser);

    Customer getCustomerById(UUID customer_id);

    RetrieveCustomerRequest RetrieveCustomerById(UUID customer_id);
}
