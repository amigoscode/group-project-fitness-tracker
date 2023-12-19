package com.project.trackfit.customer.service;

import com.project.trackfit.core.exception.RequestValidationException;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    private Customer findOrThrow(final UUID id) {
        return customerRepository.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public UUID createCustomer(ApplicationUser applicationUser){
        Customer customer = new Customer(applicationUser);

        customer.setUser(applicationUser);
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
    public Customer updateCustomer(UUID customerId,
                                   UpdateCustomerRequest updateRequest) {

        Customer customer = findOrThrow(customerId);

        boolean changes = false;

        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if(updateRequest.address() != null && !updateRequest.address().equals(customer.getAddress())) {
            customer.setAddress(updateRequest.address());
            changes = true;
        }

        if(updateRequest.role() != null && !updateRequest.role().equals(customer.getUser().getRole())) {
            customer.getUser().setRole(updateRequest.role());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException();
        }

        customerRepository.save(customer);

        return customer;
    }

    @Override
    public Customer getCustomerByUserId(UUID user_id) {
      return  customerRepository.findByUser_Id(user_id).orElseThrow(ResourceNotFoundException::new);
    }

}
