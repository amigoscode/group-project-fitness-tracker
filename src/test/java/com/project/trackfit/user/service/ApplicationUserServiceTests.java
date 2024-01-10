package com.project.trackfit.user.service;

import com.project.trackfit.user.component.Role;
import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.customer.service.ICustomerService;
import com.project.trackfit.trainer.service.IPersonalTrainerService;
import com.project.trackfit.user.component.PasswordCreation;
import com.project.trackfit.user.entity.ApplicationUser;
import com.project.trackfit.user.dto.CreateUserRequest;
import com.project.trackfit.user.repository.ApplicationUserRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.project.trackfit.user.component.Role.CUSTOMER;
import static com.project.trackfit.user.component.Role.TRAINER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

    @Captor
    ArgumentCaptor<ApplicationUser> userCaptor;

    @Test
    @DisplayName("Create an application user with role as customer")
    public void givenUserRequest_whenCreateUser_thenReturnCustomerUUID() {
        //given: the initial request
        CreateUserRequest request = createTestUserRequest(CUSTOMER);

        //and: a new application user
        ApplicationUser user = createTestApplicationUser(CUSTOMER);

        //and: the user doesn't exist already with this email
        given(applicationUserRepo.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());

        //and: save this new user
        given(applicationUserRepo.save(any(ApplicationUser.class))).willReturn(user);

        //and: this user will receive a random UUID
        UUID randomUUID = UUID.randomUUID();
        given(customerService.createCustomer(any(ApplicationUser.class))).willReturn(randomUUID);

        //when: calling the service
        UUID savedUserUUID = service.createUser(request);

        //then: the user is being created
        assertThat(savedUserUUID).isEqualTo(randomUUID);

        //and: verify the expected parameters from initial request
        verify(applicationUserRepo).save(userCaptor.capture());
        ApplicationUser savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(request.getLastName());
        assertThat(savedUser.getRole()).isEqualTo(request.getRole());
        assertThat(savedUser.getAge()).isEqualTo(request.getAge());
        assertThat(savedUser.getAddress()).isEqualTo(request.getAddress());

        //and: the right service called
        verify(customerService).createCustomer(any(ApplicationUser.class));
        verify(personalTrainerService, never()).createTrainer(any(ApplicationUser.class));
    }

    @Test
    @DisplayName("Create an application user with role as trainer")
    public void givenUserRequest_whenCreateUser_thenReturnTrainerUUID() {
        //given: the initial request
        CreateUserRequest request = createTestUserRequest(TRAINER);

        //and: a new application user
        ApplicationUser user = createTestApplicationUser(TRAINER);

        //and: the user doesn't exist already with this email
        given(applicationUserRepo.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());

        //and: save this new user
        given(applicationUserRepo.save(any(ApplicationUser.class))).willReturn(user);

        //and: this user will receive a random UUID
        UUID randomUUID = UUID.randomUUID();
        given(personalTrainerService.createTrainer(any(ApplicationUser.class))).willReturn(randomUUID);

        //when: calling the service
        UUID savedUserUUID = service.createUser(request);

        //then: the user is being created
        assertThat(savedUserUUID).isEqualTo(randomUUID);

        //and: verify the expected parameters from initial request
        verify(applicationUserRepo).save(userCaptor.capture());
        ApplicationUser savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(request.getLastName());
        assertThat(savedUser.getRole()).isEqualTo(request.getRole());
        assertThat(savedUser.getAge()).isEqualTo(request.getAge());
        assertThat(savedUser.getAddress()).isEqualTo(request.getAddress());

        //and: the right service called
        verify(personalTrainerService).createTrainer(any(ApplicationUser.class));
        verify(customerService, never()).createCustomer(any(ApplicationUser.class));
    }

    @Test
    @DisplayName("Creation of a user fails due to existing email")
    public void givenExistingEmail_whenCreateUser_thenReturnEmailAlreadyTakenException() {
        //given: the initial request
        CreateUserRequest request = createTestUserRequest(TRAINER);

        //and: a new application user
        ApplicationUser user = createTestApplicationUser(TRAINER);

        //and: the user exists already with this email
        given(applicationUserRepo.findByEmail(request.getEmail()))
                .willReturn(Optional.of(user));

        //when: calling the service we expect to get an EmailAlreadyTakenException
        assertThrows(EmailAlreadyTakenException.class, () -> service.createUser(request));

        //then: no more interactions are taking place
        verifyNoMoreInteractions(applicationUserRepo, customerService, personalTrainerService, passwordCreation);
    }

    private CreateUserRequest createTestUserRequest(Role role) {
        return new CreateUserRequest(
                "andreas.kreouzos@hotmail.com",
                "MyPassword",
                "Andreas",
                "Kreouzos",
                role,
                38,
                "Athens, Greece");
    }

    private ApplicationUser createTestApplicationUser(Role role) {
        return new ApplicationUser(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                role,
                38,
                "Athens, Greece");
    }
}
