package com.project.trackfit.user;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;

import com.project.trackfit.customer.ICustomerService;
import com.project.trackfit.trainer.IPersonalTrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ICustomerService customerService;
    private final IPersonalTrainerService trainerService;
    private final PasswordCreation password;

    @Autowired
    public UserService(UserRepository userRepository,
                       ICustomerService customerService,
                       IPersonalTrainerService trainerService,
                       PasswordCreation password) {
        this.userRepository = userRepository;
        this.customerService = customerService;
        this.trainerService = trainerService;
        this.password = password;
    }

    @Override
    public UUID createUser(UserCreationRequest userCreationRequest) {
        checkEmailExists(userCreationRequest.getEmail());
        byte[] salt = password.createSalt();
        byte[] hashedPassword = password.createPasswordHash(userCreationRequest.getPassword(), salt);
        User user = createUser(userCreationRequest, salt, hashedPassword);
        return assignUserRole(user);
    }

    private void checkEmailExists(String email) {
        userRepository.findByEmail(email)
                .ifPresent(u -> {
                    throw new EmailAlreadyTakenException(email);
                });
    }

    private User createUser(UserCreationRequest userCreationRequest, byte[] salt, byte[] hashedPassword) {
        User user = new User(
                userCreationRequest.getEmail(),
                userCreationRequest.getFirstName(),
                userCreationRequest.getLastName(),
                salt,
                hashedPassword,
                userCreationRequest.getRole(),
                userCreationRequest.getAge(),
                userCreationRequest.getAddress(),
                userCreationRequest.getPhoneNumber()
        );
        userRepository.save(user);
        return user;
    }

    private UUID assignUserRole(User user) {
        if (user.getRole() == Role.CUSTOMER) {
            return customerService.createCustomer(user);
        } else {
            return trainerService.createTrainer(user);
        }
    }
}