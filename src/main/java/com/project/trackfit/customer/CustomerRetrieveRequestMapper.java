package com.project.trackfit.customer;

import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CustomerRetrieveRequestMapper  implements Function<Customer,RetrieveCustomerRequest> {
    @Override
    public RetrieveCustomerRequest apply(Customer customer) {
        return new RetrieveCustomerRequest(
                customer.getCustomer_id(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAge()
        );
    }
}
