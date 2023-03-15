package com.project.trackfit.customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    UUID createCustomer(CreateCustomerRequest createCustomerRequest);
    Customer getID(UUID customer_id);
    RetrieveCustomerRequest getCustomerById(UUID userId);
}