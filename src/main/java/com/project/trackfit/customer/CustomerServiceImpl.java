package com.project.trackfit.customer;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.registration.EmailValidator;
import com.project.trackfit.core.util.SaltHelper;
import com.project.trackfit.trainer.CreateTrainerRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EmailValidator emailValidator;
    private final CustomerRetrieveRequestMapper customerRetrieveRequestMapper;
    private byte [] createSalt(){
        var random = new SecureRandom();
        var salt =new byte[128];
        random.nextBytes(salt);
        return salt;

    }

    private byte[] createPasswordHash(String password , byte[]salt) throws NoSuchAlgorithmException{
        var md= MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public UUID createCustomer(CreateCustomerRequest createCustomerRequest)  throws NoSuchAlgorithmException {
        checkEmailValidity(createCustomerRequest.email());
        checkEmailExists(createCustomerRequest.email());
        if(createCustomerRequest.password().isBlank()) throw new IllegalArgumentException(
                "Password is required"
        );
        byte[] salt= createSalt();
        byte[] hashedPassword=
               createPasswordHash(createCustomerRequest.password(), salt);
        Customer customer= new Customer(
                createCustomerRequest.firstName(),
                createCustomerRequest.lastName(),
                createCustomerRequest.address(),
                createCustomerRequest.age(),
                createCustomerRequest.email(),
                salt,
                hashedPassword


        );
        System.out.println(customer.getEmail());
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

    private void checkEmailValidity(String email) {
        if (!(emailValidator.checkMailPattern(email))) {
            throw new EmailNotValidException();
        }
    }

    private void checkEmailExists(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException();
        }
    }
}
