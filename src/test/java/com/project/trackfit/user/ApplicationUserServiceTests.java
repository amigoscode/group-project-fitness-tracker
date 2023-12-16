package com.project.trackfit.user;

import com.project.trackfit.core.Role;
import com.project.trackfit.customer.CreateCustomerRequest;
import com.project.trackfit.customer.ICustomerService;
import com.project.trackfit.trainer.IPersonalTrainerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserServiceTests {

    @Mock
    private ApplicationUserRepo applicationUserRepo;

    @Mock
    private ICustomerService customerService;

    @Mock
    private IPersonalTrainerService personalTrainerService;

    @InjectMocks
    private ApplicationUserService service;

    @Test
    @DisplayName("Create an application user with correct request")
    public void givenUserRequest_whenCreateUser_thenReturnApplicationUser() {
        //given - precondition or setup
        CreateUserRequest request = new CreateUserRequest(
                "andreas.kreouzos@hotmail.com",
                "MyPassword",
                "Andreas",
                "Kreouzos",
                Role.CUSTOMER,
                38,
                "Athens, Greece"
        );

        ApplicationUser user = new ApplicationUser(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                Role.CUSTOMER
        );

        given(applicationUserRepo.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());

        given(applicationUserRepo.save(any(ApplicationUser.class))).willReturn(user);

        UUID dummyUUID = UUID.randomUUID();
        given(customerService.createCustomer(any(ApplicationUser.class), any(CreateCustomerRequest.class))).willReturn(dummyUUID);

        // When - action or the behaviour that we are going to test
        UUID savedUserUUID = service.createUser(request);

        // Then - verify the output
        assertThat(savedUserUUID).isEqualTo(dummyUUID);
    }
}
