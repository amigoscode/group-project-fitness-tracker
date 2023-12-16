package com.project.trackfit.customer;

import com.project.trackfit.user.dto.ApplicationUser;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CustomerRetrieveRequestMapper  implements Function<Customer,RetrieveCustomerRequest> {
    @Override
    public RetrieveCustomerRequest apply(Customer customer) {
        ApplicationUser user = customer.getUser();
        if (user == null) {
            throw new IllegalStateException("User is null for customer with ID " + customer.getId());
        }

        return new RetrieveCustomerRequest(
                customer.getId(),
                user.getFirstName(),
                user.getLastName(),
                customer.getAge(),
                user.getEmail(),
                customer.getAddress()
        );
    }
}
