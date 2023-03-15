package com.project.trackfit.security.jwt;

import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerRepository;
import com.project.trackfit.trainer.PersonalTrainer;
import com.project.trackfit.trainer.PersonalTrainerRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
@AllArgsConstructor
public class ApplicationConfig {
    private final CustomerRepository customerRepository;
    private final PersonalTrainerRepo personalTrainerRepo;

    public UserDetails customerDetailsService(String username){
        return customerRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Customer not found"));
    }


    public UserDetails trainerDetailsService(String username){
        return  personalTrainerRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Personal Trainer not found"));
    }
    public Customer authenticateCustomer(String email, String password)
            throws NoSuchAlgorithmException {
        if (
                email.isEmpty() || password.isEmpty()
        ) throw new BadCredentialsException("Unauthorized");

        var customerEntity = customerRepository.findByEmail(email);

        if (customerEntity == null) throw new BadCredentialsException("Unauthorized");

        var verified = verifyPasswordHash(
                password,
                customerEntity.get().getStoredHash(),
                customerEntity.get().getStoredSalt()
        );

        if (!verified) throw new BadCredentialsException("Unauthorized");

        return customerEntity.get();
    }
    public PersonalTrainer authenticateTrainer(String email, String password)
            throws NoSuchAlgorithmException {
        if (
                email.isEmpty() || password.isEmpty()
        ) throw new BadCredentialsException("Unauthorized");

        var trainerEntity = personalTrainerRepo.findByEmail(email);

        if (trainerEntity == null) throw new BadCredentialsException("Unauthorized");

        var verified = verifyPasswordHash(
                password,
                trainerEntity.get().getStoredHash(),
                trainerEntity.get().getStoredSalt()
        );

        if (!verified) throw new BadCredentialsException("Unauthorized");

        return trainerEntity.get();
    }
    private Boolean verifyPasswordHash(
            String password,
            byte[] storedHash,
            byte[] storedSalt
    ) throws NoSuchAlgorithmException {
        if (
                password.isBlank() || password.isEmpty()
        ) throw new IllegalArgumentException(
                "Password cannot be empty or whitespace only string."
        );

        if (storedHash.length != 64) throw new IllegalArgumentException(
                "Invalid length of password hash (64 bytes expected)"
        );

        if (storedSalt.length != 128) throw new IllegalArgumentException(
                "Invalid length of password salt (64 bytes expected)."
        );

        var md = MessageDigest.getInstance("SHA-512");
        md.update(storedSalt);

        var computedHash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        for (int i = 0; i < computedHash.length; i++) {
            if (computedHash[i] != storedHash[i]) return false;
        }

        // The above for loop is the same as below

        return MessageDigest.isEqual(computedHash, storedHash);
    }

}
