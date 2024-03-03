package com.project.trackfit.user;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.customer.ICustomerService;
import com.project.trackfit.trainer.IPersonalTrainerService;
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

import static com.project.trackfit.user.Role.CUSTOMER;
import static com.project.trackfit.user.Role.TRAINER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ICustomerService customerService;

    @Mock
    private PasswordCreation passwordCreation;

    @Mock
    private IPersonalTrainerService personalTrainerService;

    @InjectMocks
    private UserService service;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Create an application user with role as customer")
    public void givenUserRequest_whenCreateUser_thenReturnCustomerUUID() {
        //given: the initial request
        UserCreationRequest request = createTestUserRequest(CUSTOMER);

        //and: a new application user
        User user = createTestApplicationUser(CUSTOMER);

        //and: the user doesn't exist already with this email
        given(userRepository.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());

        //and: save this new user
        given(userRepository.save(any(User.class))).willReturn(user);

        //and: this user will receive a random UUID
        UUID randomUUID = UUID.randomUUID();
        given(customerService.createCustomer(any(User.class))).willReturn(randomUUID);

        //when: calling the service
        UUID savedUserUUID = service.createUser(request);

        //then: the user is being created
        assertThat(savedUserUUID).isEqualTo(randomUUID);

        //and: verify the expected parameters from initial request
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(request.getLastName());
        assertThat(savedUser.getRole()).isEqualTo(request.getRole());
        assertThat(savedUser.getAge()).isEqualTo(request.getAge());
        assertThat(savedUser.getAddress()).isEqualTo(request.getAddress());

        //and: the right service called
        verify(customerService).createCustomer(any(User.class));
        verify(personalTrainerService, never()).createTrainer(any(User.class));
    }

    @Test
    @DisplayName("Create an application user with role as trainer")
    public void givenUserRequest_whenCreateUser_thenReturnTrainerUUID() {
        //given: the initial request
        UserCreationRequest request = createTestUserRequest(TRAINER);

        //and: a new application user
        User user = createTestApplicationUser(TRAINER);

        //and: the user doesn't exist already with this email
        given(userRepository.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());

        //and: save this new user
        given(userRepository.save(any(User.class))).willReturn(user);

        //and: this user will receive a random UUID
        UUID randomUUID = UUID.randomUUID();
        given(personalTrainerService.createTrainer(any(User.class))).willReturn(randomUUID);

        //when: calling the service
        UUID savedUserUUID = service.createUser(request);

        //then: the user is being created
        assertThat(savedUserUUID).isEqualTo(randomUUID);

        //and: verify the expected parameters from initial request
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(request.getLastName());
        assertThat(savedUser.getRole()).isEqualTo(request.getRole());
        assertThat(savedUser.getAge()).isEqualTo(request.getAge());
        assertThat(savedUser.getAddress()).isEqualTo(request.getAddress());

        //and: the right service called
        verify(personalTrainerService).createTrainer(any(User.class));
        verify(customerService, never()).createCustomer(any(User.class));
    }

    @Test
    @DisplayName("Creation of a user fails due to existing email")
    public void givenExistingEmail_whenCreateUser_thenReturnEmailAlreadyTakenException() {
        //given: the initial request
        UserCreationRequest request = createTestUserRequest(TRAINER);

        //and: a new application user
        User user = createTestApplicationUser(TRAINER);

        //and: the user exists already with this email
        given(userRepository.findByEmail(request.getEmail()))
                .willReturn(Optional.of(user));

        //when: calling the service we expect to get an EmailAlreadyTakenException
        assertThrows(EmailAlreadyTakenException.class, () -> service.createUser(request));

        //then: no more interactions are taking place
        verifyNoMoreInteractions(userRepository, customerService, personalTrainerService, passwordCreation);
    }

    private UserCreationRequest createTestUserRequest(Role role) {
        return new UserCreationRequest(
                "andreas.kreouzos@hotmail.com",
                "MyPassword",
                "Andreas",
                "Kreouzos",
                role,
                38,
                "Athens, Greece",
                "00306931234567");
    }

    private User createTestApplicationUser(Role role) {
        return new User(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                role,
                38,
                "Athens, Greece",
                "00306931234567");
    }
}
