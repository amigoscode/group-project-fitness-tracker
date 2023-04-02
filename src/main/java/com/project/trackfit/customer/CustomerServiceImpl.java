package com.project.trackfit.customer;

import com.project.trackfit.core.ApplicationUser;

import com.project.trackfit.core.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRetrieveRequestMapper customerRetrieveRequestMapper;
    private Customer findOrThrow(final UUID id) {
        return customerRepository.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }
    @Override
    public UUID createCustomer(ApplicationUser applicationUser){
        Customer customer= new Customer(
                applicationUser
        );
        System.out.println(customer.getUser().getEmail());
        customerRepository.save(customer);

        return customer.getId();
    }

    @Override
    public Customer getCustomerById(UUID userId) {
        return findOrThrow(userId);
    }

    @Override
    public RetrieveCustomerRequest updateCustomer(
            UUID customerId,
            UpdateCustomerRequest updateCustomerRequest) {

        var customer=findOrThrow(customerId);
        if(updateCustomerRequest.age() !=null) {
            customer.setAge(updateCustomerRequest.age());
        }
        if(updateCustomerRequest.address() !=null) {
            customer.setAddress(updateCustomerRequest.address());
        }
        customerRepository.save(customer);

        return  customerRetrieveRequestMapper.apply(customer);
    }

    @Override
    public RetrieveCustomerRequest RetrieveCustomerById(UUID customer_id) {
        return customerRepository
                .findById(customer_id)
                .map(customerRetrieveRequestMapper)
                .orElseThrow(ResourceNotFoundException::new);
    }

}
