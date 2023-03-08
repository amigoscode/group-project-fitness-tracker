package com.project.trackfit.customer;

import java.util.UUID;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(UUID userId);
}
