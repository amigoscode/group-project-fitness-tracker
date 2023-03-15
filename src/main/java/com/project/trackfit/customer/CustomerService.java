package com.project.trackfit.customer;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface CustomerService {
    UUID createCustomer(CreateCustomerRequest createCustomerRequest) throws NoSuchAlgorithmException;

    Customer getCustomerById(UUID customer_id);

    RetrieveCustomerRequest RetrieveCustomerById(UUID customer_id);
}
