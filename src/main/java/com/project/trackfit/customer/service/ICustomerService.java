package com.project.trackfit.customer.service;

import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.user.entity.ApplicationUser;

import java.util.UUID;

public interface ICustomerService {
    UUID createCustomer(ApplicationUser applicationUser);
    Customer getCustomerById(UUID customer_id);
    Customer updateCustomer(UUID customerId, UpdateCustomerRequest updateCustomerRequest);
}