package com.project.trackfit.security.jwt;

import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.core.ApplicationUserRepo;
import com.project.trackfit.core.ApplicationUserService;
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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
@AllArgsConstructor
public class ApplicationConfig implements UserDetailsService {

    private final ApplicationUserRepo applicationUserRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return applicationUserRepo.findByEmail(email).get();
    }

    public ApplicationUser authenticate(String email, String password)
            throws NoSuchAlgorithmException {
        if (
                email.isEmpty() || password.isEmpty()
        ) throw new BadCredentialsException("Unauthorized");

        var userEntity = applicationUserRepo.findByEmail(email);

        if (userEntity == null) throw new BadCredentialsException("Unauthorized");

        var verified = verifyPasswordHash(
                password,
                userEntity.get().getStoredHash(),
                userEntity.get().getStoredSalt()
        );

        if (!verified) throw new BadCredentialsException("Unauthorized");

        return userEntity.get();
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

        return MessageDigest.isEqual(computedHash, storedHash);
    }

}
