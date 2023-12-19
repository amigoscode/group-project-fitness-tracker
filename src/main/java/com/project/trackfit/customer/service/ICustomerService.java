package com.project.trackfit.customer.service;

import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.user.dto.ApplicationUser;

import java.util.UUID;

public interface ICustomerService {
    UUID createCustomer(ApplicationUser applicationUser);
    Customer getCustomerById(UUID customer_id);
    Customer updateCustomer(UUID customerId, UpdateCustomerRequest updateCustomerRequest);
    Customer getCustomerByUserId(UUID user_id);
}