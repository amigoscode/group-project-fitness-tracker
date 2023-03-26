package com.project.trackfit.customer;

import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRetrieveRequestMapper customerRetrieveRequestMapper;

    @Override
    public UUID createCustomer(ApplicationUser applicationUser, CreateCustomerRequest createCustomerRequest){
        Customer customer = new Customer(
                applicationUser
        );
        customer.setUser(applicationUser);
        customer.setAge(createCustomerRequest.age());
        customer.setAddress(createCustomerRequest.address());
        customerRepository.save(customer);
        return customer.getId();
    }

    @Override
    public Customer getCustomerById(UUID userId) {
        return customerRepository
                .findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public RetrieveCustomerRequest RetrieveCustomerById(UUID customer_id) {
        return customerRepository
                .findById(customer_id)
                .map(customerRetrieveRequestMapper)
                .orElseThrow(ResourceNotFoundException::new);
    }
}