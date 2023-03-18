package com.project.trackfit.security.jwt;

import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerRepository;
import com.project.trackfit.trainer.PersonalTrainer;
import com.project.trackfit.trainer.PersonalTrainerRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.function.Function;


@Service
@AllArgsConstructor
public class ApplicationConfig {

    private final CustomerRepository customerRepository;
    private final PersonalTrainerRepo personalTrainerRepo;

    public UserDetails customerDetailsService(String username) {
        return customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
    }

    public UserDetails trainerDetailsService(String username) {
        return personalTrainerRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Personal Trainer not found"));
    }

    public Customer authenticateCustomer(String email, String password)
            throws NoSuchAlgorithmException {
        return doAuthentication(
                email,
                password,
                customerRepository::findByEmail,
                Customer::getStoredHash,
                Customer::getStoredSalt
        );
    }

    public PersonalTrainer authenticateTrainer(String email, String password)
            throws NoSuchAlgorithmException {
        return doAuthentication(
                email,
                password,
                personalTrainerRepo::findByEmail,
                PersonalTrainer::getStoredHash,
                PersonalTrainer::getStoredSalt
        );
    }

    private <T> T doAuthentication(String email,
                                   String password,
                                   Function<String, Optional<T>> findByEmailFunction,
                                   Function<T, byte[]> getStoredHashFunction,
                                   Function<T, byte[]> getStoredSaltFunction)
            throws NoSuchAlgorithmException {

        if (email.isEmpty() || password.isEmpty())
            throw new BadCredentialsException("Unauthorized");

        Optional<T> entity = findByEmailFunction.apply(email);

        if (entity.isEmpty()) {
            throw new BadCredentialsException("Unauthorized");
        }

        var storedHash = getStoredHashFunction.apply(entity.get());
        var storedSalt = getStoredSaltFunction.apply(entity.get());
        var verified = verifyPasswordHash(password, storedHash, storedSalt);

        if (!verified){
            throw new BadCredentialsException("Unauthorized");
        }
        return entity.get();
    }

    private Boolean verifyPasswordHash(String password, byte[] storedHash, byte[] storedSalt)
            throws NoSuchAlgorithmException {
        if (password.isBlank() || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty or whitespace only string.");
        }

        if (storedHash.length != 64) {
            throw new IllegalArgumentException("Invalid length of password hash (64 bytes expected)");
        }

        if (storedSalt.length != 128) {
            throw new IllegalArgumentException("Invalid length of password salt (64 bytes expected).");
        }

        var md = MessageDigest.getInstance("SHA-512");
        md.update(storedSalt);

        var computedHash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        for (int i = 0; i < computedHash.length; i++) {
            if (computedHash[i] != storedHash[i])
                return false;
        }

        // The above for loop is the same as below

        return MessageDigest.isEqual(computedHash, storedHash);
    }
}
