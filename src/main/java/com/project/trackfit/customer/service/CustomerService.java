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
        Customer customer = getCustomerById(customerId);
        ApplicationUser user = customer.getUser();

        boolean changes = false;

        if(updateRequest.getAge() != null && !updateRequest.getAge().equals(customer.getAge())) {
            customer.setAge(updateRequest.getAge());
            user.setAge(updateRequest.getAge());
            changes = true;
        }

        if(updateRequest.getAddress() != null && !updateRequest.getAddress().equals(customer.getAddress())) {
            customer.setAddress(updateRequest.getAddress());
            user.setAddress(updateRequest.getAddress());
            changes = true;
        }

        if(updateRequest.getRole() != null && !updateRequest.getRole().equals(customer.getUser().getRole())) {
            customer.getUser().setRole(updateRequest.getRole());
            user.setRole(updateRequest.getRole());
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
