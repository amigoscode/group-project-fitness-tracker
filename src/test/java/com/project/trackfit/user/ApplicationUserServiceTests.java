package com.project.trackfit.user;

import com.project.trackfit.core.Role;
import com.project.trackfit.customer.ICustomerService;
import com.project.trackfit.trainer.IPersonalTrainerService;
import com.project.trackfit.user.component.PasswordCreation;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.user.dto.CreateUserRequest;
import com.project.trackfit.user.repository.ApplicationUserRepo;
import com.project.trackfit.user.service.ApplicationUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserServiceTests {

    @Mock
    private ApplicationUserRepo applicationUserRepo;

    @Mock
    private ICustomerService customerService;

    @Mock
    private PasswordCreation passwordCreation;

    @Mock
    private IPersonalTrainerService personalTrainerService;

    @InjectMocks
    private ApplicationUserService service;

    @Test
    @DisplayName("Create an application user with role as customer")
    public void givenUserRequest_whenCreateUser_thenReturnCustomerUUID() {
        //given: the initial request
        CreateUserRequest request = new CreateUserRequest(
                "andreas.kreouzos@hotmail.com",
                "MyPassword",
                "Andreas",
                "Kreouzos",
                Role.CUSTOMER,
                38,
                "Athens, Greece"
        );

        //and: a new application user
        ApplicationUser user = new ApplicationUser(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                Role.CUSTOMER,
                38,
                "Athens, Greece"
        );

        //and: the user doesn't exist already with this email
        given(applicationUserRepo.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());

        //and: creating the password for this new user
        given(passwordCreation.createSalt()).willReturn(new byte[128]);
        given(passwordCreation.createPasswordHash(anyString(), any(byte[].class))).willReturn(new byte[64]);

        //and: save this new user
        given(applicationUserRepo.save(any(ApplicationUser.class))).willReturn(user);

        //and: this user will receive a random UUID
        UUID randomUUID = UUID.randomUUID();
        given(customerService.createCustomer(any(ApplicationUser.class))).willReturn(randomUUID);

        //when: calling the service
        UUID savedUserUUID = service.createUser(request);

        //then: the user is being created
        assertThat(savedUserUUID).isEqualTo(randomUUID);
    }

    @Test
    @DisplayName("Create an application user with role as trainer")
    public void givenUserRequest_whenCreateUser_thenReturnTrainerUUID() {
        //given: the initial request
        CreateUserRequest request = new CreateUserRequest(
                "andreas.kreouzos@hotmail.com",
                "MyPassword",
                "Andreas",
                "Kreouzos",
                Role.TRAINER,
                38,
                "Athens, Greece"
        );

        //and: a new application user
        ApplicationUser user = new ApplicationUser(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                Role.TRAINER,
                38,
                "Athens, Greece"
        );

        //and: the user doesn't exist already with this email
        given(applicationUserRepo.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());

        //and: creating the password for this new user
        given(passwordCreation.createSalt()).willReturn(new byte[128]);
        given(passwordCreation.createPasswordHash(anyString(), any(byte[].class))).willReturn(new byte[64]);

        //and: save this new user
        given(applicationUserRepo.save(any(ApplicationUser.class))).willReturn(user);

        //and: this user will receive a random UUID
        UUID randomUUID = UUID.randomUUID();
        given(personalTrainerService.createTrainer(any(ApplicationUser.class))).willReturn(randomUUID);

        //when: calling the service
        UUID savedUserUUID = service.createUser(request);

        //then: the user is being created
        assertThat(savedUserUUID).isEqualTo(randomUUID);
    }
}
