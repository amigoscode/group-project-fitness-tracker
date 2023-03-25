package com.project.trackfit.core;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.validation.EmailValidator;

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
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final ApplicationUserRepo applicationUserRepo;
    private final ICustomerService ICustomerService;
    private final IPersonalTrainerService trainerService;
    private final EmailValidator emailValidator;

    @Override
    public UUID createUser(CreateUserRequest createUserRequest) throws NoSuchAlgorithmException {
        checkEmailValidity(createUserRequest);
        checkEmailExists(createUserRequest.email());
        if(createUserRequest.password().isBlank())
            throw new IllegalArgumentException(
                "Password is required"
        );
        if(createUserRequest.role().isBlank())
            throw new IllegalArgumentException(
                "role is required"
        );
        byte[] salt = createSalt();
        byte[] hashedPassword =
                createPasswordHash(createUserRequest.password(), salt);

        ApplicationUser applicationUser = createUser(createUserRequest, salt, hashedPassword);

        return assignUserRole(createUserRequest, applicationUser);
    }

    private void checkEmailValidity(CreateUserRequest user) {
        if (!(emailValidator.checkMailPattern(user.email()))) {
            throw new EmailNotValidException();
        }
    }

    private void checkEmailExists(String email) {
        if (applicationUserRepo.existsByEmail(email)) {
            throw new EmailAlreadyTakenException();
        }
    }

    private byte[] createSalt() {
        var random = new SecureRandom();
        var salt = new byte[128];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] createPasswordHash(String password , byte[] salt) throws NoSuchAlgorithmException {
        var md= MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
    }

    @NotNull
    private ApplicationUser createUser(CreateUserRequest createUserRequest, byte[] salt, byte[] hashedPassword) {
        ApplicationUser applicationUser = new ApplicationUser(
                createUserRequest.email(),
                createUserRequest.firstName(),
                createUserRequest.lastName(),
                salt,
                hashedPassword,
               Role.valueOf(createUserRequest.role())
        );
        applicationUserRepo.save(applicationUser);
        return applicationUser;
    }

    private UUID assignUserRole(CreateUserRequest createUserRequest, ApplicationUser applicationUser) {
        if(applicationUser.getRole() == Role.CUSTOMER) {
            CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(
                    createUserRequest.firstName(),
                    createUserRequest.lastName(),
                    createUserRequest.age(),
                    createUserRequest.email(),
                    createUserRequest.address(),
                    createUserRequest.password()
            );
            return ICustomerService.createCustomer(applicationUser, createCustomerRequest);
        } else {
            return trainerService.createTrainer(applicationUser);
        }
    }
}