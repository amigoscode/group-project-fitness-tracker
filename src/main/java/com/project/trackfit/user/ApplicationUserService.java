package com.project.trackfit.user;

import com.project.trackfit.core.Role;
import com.project.trackfit.core.exception.EmailAlreadyTakenException;

import com.project.trackfit.customer.CreateCustomerRequest;
import com.project.trackfit.customer.ICustomerService;
import com.project.trackfit.trainer.IPersonalTrainerService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApplicationUserService implements IApplicationUserService {

    private final ApplicationUserRepo applicationUserRepo;
    private final ICustomerService customerService;
    private final IPersonalTrainerService trainerService;

    @Override
    public UUID createUser(CreateUserRequest createUserRequest) {
        checkEmailExists(createUserRequest.getEmail());
        byte[] salt = createSalt();
        byte[] hashedPassword = createPasswordHash(createUserRequest.getPassword(), salt);
        ApplicationUser applicationUser = createUser(createUserRequest, salt, hashedPassword);
        return assignUserRole(createUserRequest, applicationUser);
    }

    private void checkEmailExists(String email) {
        applicationUserRepo.findByEmail(email)
                .ifPresent(u -> {
                    throw new EmailAlreadyTakenException();
                });
    }

    private byte[] createSalt() {
        var random = new SecureRandom();
        var salt = new byte[128];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] createPasswordHash(String password, byte[] salt) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(salt);
        return md.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
    }

    @NotNull
    private ApplicationUser createUser(CreateUserRequest createUserRequest, byte[] salt, byte[] hashedPassword) {
        ApplicationUser applicationUser = new ApplicationUser(
                createUserRequest.getEmail(),
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                salt,
                hashedPassword,
                createUserRequest.getRole()
        );
        applicationUserRepo.save(applicationUser);
        return applicationUser;
    }

    private UUID assignUserRole(CreateUserRequest createUserRequest, ApplicationUser applicationUser) {
        if (applicationUser.getRole() == Role.CUSTOMER) {
            CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(
                    createUserRequest.getFirstName(),
                    createUserRequest.getLastName(),
                    createUserRequest.getAge(),
                    createUserRequest.getEmail(),
                    createUserRequest.getAddress(),
                    createUserRequest.getPassword()
            );
            return customerService.createCustomer(applicationUser, createCustomerRequest);
        } else {
            return trainerService.createTrainer(applicationUser);
        }
    }
}