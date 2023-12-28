package com.project.trackfit.user.service;

import com.project.trackfit.user.component.Role;
import com.project.trackfit.core.exception.EmailAlreadyTakenException;

import com.project.trackfit.customer.service.ICustomerService;
import com.project.trackfit.trainer.service.IPersonalTrainerService;
import com.project.trackfit.user.component.PasswordCreation;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.user.repository.ApplicationUserRepo;
import com.project.trackfit.user.dto.CreateUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ApplicationUserService implements IApplicationUserService {

    private final ApplicationUserRepo applicationUserRepo;
    private final ICustomerService customerService;
    private final IPersonalTrainerService trainerService;
    private final PasswordCreation password;

    @Override
    public UUID createUser(CreateUserRequest createUserRequest) {
        checkEmailExists(createUserRequest.getEmail());
        byte[] salt = password.createSalt();
        byte[] hashedPassword = password.createPasswordHash(createUserRequest.getPassword(), salt);
        ApplicationUser applicationUser = createUser(createUserRequest, salt, hashedPassword);
        return assignUserRole(applicationUser);
    }

    private void checkEmailExists(String email) {
        applicationUserRepo.findByEmail(email)
                .ifPresent(u -> {
                    throw new EmailAlreadyTakenException(email);
                });
    }

    private ApplicationUser createUser(CreateUserRequest createUserRequest, byte[] salt, byte[] hashedPassword) {
        ApplicationUser applicationUser = new ApplicationUser(
                createUserRequest.getEmail(),
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                salt,
                hashedPassword,
                createUserRequest.getRole(),
                createUserRequest.getAge(),
                createUserRequest.getAddress()
        );
        applicationUserRepo.save(applicationUser);
        return applicationUser;
    }

    private UUID assignUserRole(ApplicationUser applicationUser) {
        if (applicationUser.getRole() == Role.CUSTOMER) {
            return customerService.createCustomer(applicationUser);
        } else {
            return trainerService.createTrainer(applicationUser);
        }
    }
}