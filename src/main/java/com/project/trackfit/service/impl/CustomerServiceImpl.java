package com.project.trackfit.service.impl;

import com.project.trackfit.model.Customer;
import com.project.trackfit.repository.CustomerRepository;
import com.project.trackfit.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
