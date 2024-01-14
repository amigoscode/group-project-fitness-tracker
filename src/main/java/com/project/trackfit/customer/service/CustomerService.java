package com.project.trackfit.customer.service;

import com.project.trackfit.core.exception.RequestValidationException;
import com.project.trackfit.customer.dto.CustomerResponse;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.user.entity.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UUID createCustomer(ApplicationUser applicationUser) {
        Customer customer = new Customer(applicationUser);
        customerRepository.save(customer);
        return customer.getId();
    }

    @Override
    public CustomerResponse getCustomerById(UUID userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        return mapToCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(
            UUID customerId,
            UpdateCustomerRequest updateRequest) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(ResourceNotFoundException::new);

        boolean changes = false;

        if (updateRequest.getAge() != null && !updateRequest.getAge().equals(customer.getUser().getAge())) {
            customer.getUser().setAge(updateRequest.getAge());
            changes = true;
        }

        if (updateRequest.getAddress() != null && !updateRequest.getAddress().equals(customer.getUser().getAddress())) {
            customer.getUser().setAddress(updateRequest.getAddress());
            changes = true;
        }

        if (updateRequest.getRole() != null && !updateRequest.getRole().equals(customer.getUser().getRole())) {
            customer.getUser().setRole(updateRequest.getRole());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException();
        }

        customerRepository.save(customer);
        return mapToCustomerResponse(customer);
    }

    private CustomerResponse mapToCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getUser().getFirstName(),
                customer.getUser().getLastName(),
                customer.getUser().getAge(),
                customer.getUser().getEmail(),
                customer.getUser().getAddress(),
                customer.getUser().getRole()
        );
    }
}
