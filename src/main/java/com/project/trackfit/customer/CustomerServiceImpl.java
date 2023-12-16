package com.project.trackfit.customer;

import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRetrieveRequestMapper customerRetrieveRequestMapper;

    private Customer findOrThrow(final UUID id) {
        return customerRepository.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

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
        if(updateCustomerRequest.role()!=null){
            customer.getUser().setRole(updateCustomerRequest.role());
        }
        customerRepository.save(customer);

        return  customerRetrieveRequestMapper.apply(customer);
    }

    @Override
    public Customer getCustomerByUserId(UUID user_id) {
      return  customerRepository.findByUser_Id(user_id).orElseThrow(ResourceNotFoundException::new);
    }

}
