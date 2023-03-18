package com.project.trackfit.customer;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerRetrieveRequestMapper implements Function<Customer,RetrieveCustomerRequest> {
    @Override
    public RetrieveCustomerRequest apply(Customer customer) {
        return new RetrieveCustomerRequest(
                customer.getId(),
                customer.getUser().getFirstName(),
                customer.getUser().getLastName(),
                customer.getAge(),
                customer.getUser().getEmail(),
                customer.getAddress()
        );
    }
}