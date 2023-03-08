package com.project.trackfit.customer;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.registration.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EmailValidator emailValidator;

    @Override
    public Customer createCustomer(Customer customer) {
        checkEmailValidity(customer);
        checkEmailExists(customer.getEmail());
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(UUID userId) {
        return customerRepository
                .findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private void checkEmailValidity(Customer customer) {
        if (!(emailValidator.checkMailPattern(customer.getEmail()))){
            throw new EmailNotValidException();
        }
    }

    private void checkEmailExists(String email){
        if (customerRepository.existsByEmail(email)){
            throw new EmailAlreadyTakenException();
        }
    }
}
