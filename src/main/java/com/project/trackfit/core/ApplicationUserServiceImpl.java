package com.project.trackfit.core;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.validation.EmailValidator;

import com.project.trackfit.customer.CustomerService;
import com.project.trackfit.trainer.PersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApplicationUserServiceImpl implements ApplicationUserService {

    final private ApplicationUserRepo applicationUserRepo;
    final private CustomerService customerService;
    final private PersonalTrainerService trainerService;
    private  final EmailValidator emailValidator;

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
        var salt =new byte[128];
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

    @Override
    public UUID createUser(CreateUserRequest createUserRequest) throws NoSuchAlgorithmException {
        checkEmailValidity(createUserRequest);
        checkEmailExists(createUserRequest.email());
        if(createUserRequest.password().isBlank())throw  new IllegalArgumentException(
                "Password is required"
        );
        if(createUserRequest.role().isBlank())throw new IllegalArgumentException(
                "role is required"
        );
        byte[] salt= createSalt();
        byte[] hashedPassword=
                createPasswordHash(createUserRequest.password(), salt);

        //CREATE THE USER
        ApplicationUser applicationUser= new ApplicationUser(
                createUserRequest.email(),
                createUserRequest.firstName(),
                createUserRequest.lastName(),
                salt,
                hashedPassword,
               Role.valueOf(createUserRequest.role())
        );
        applicationUserRepo.save(applicationUser);

        //CHECK THE USER ROLE
        if(applicationUser.getRole() == Role.CUSTOMER){
            return customerService.createCustomer(applicationUser);
        } else {
            return trainerService.createTrainer(applicationUser);
        }
    }
}
