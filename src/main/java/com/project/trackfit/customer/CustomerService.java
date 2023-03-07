package com.project.trackfit.customer;

import com.project.trackfit.customer.Customer;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(Long userId);
}
