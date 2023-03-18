package com.project.trackfit.customer;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.validation.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EmailValidator emailValidator;
    private final CustomerRetrieveRequestMapper retrieveRequestMapper;

    @Override
    public UUID createCustomer(CreateCustomerRequest createCustomerRequest) {
        checkEmailValidity(createCustomerRequest);
        checkEmailExists(createCustomerRequest.email());
        Customer customer = new Customer(
                UUID.randomUUID(),
                createCustomerRequest.firstName(),
                createCustomerRequest.lastName(),
                createCustomerRequest.age(),
                createCustomerRequest.email(),
                createCustomerRequest.address()
        );
        customerRepository.save(customer);
        return customer.getId();
    }

    @Override
    public Customer getID(UUID customer_id) {
        return customerRepository
                .findById(customer_id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public RetrieveCustomerRequest getCustomerById(UUID userId) {
        return customerRepository
                .findById(userId)
                .map(retrieveRequestMapper)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private void checkEmailValidity(CreateCustomerRequest customer) {
        if (!(emailValidator.checkMailPattern(customer.email()))){
            throw new EmailNotValidException();
        }
    }

    private void checkEmailExists(String email){
        if (customerRepository.existsByEmail(email)){
            throw new EmailAlreadyTakenException();
        }
    }
}