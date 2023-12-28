package com.project.trackfit.customer.service;

import com.project.trackfit.core.exception.RequestValidationException;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.user.component.Role;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private ApplicationUser testApplicationUser;

    private EasyRandom easyRandom;

    @BeforeEach
    public void setUp() {
        testApplicationUser = new ApplicationUser (
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                Role.CUSTOMER,
                38,
                "Athens, Greece"
        );
        easyRandom = new EasyRandom();
    }

    @Test
    @DisplayName("Successfully create a customer")
    public void givenUUID_whenCreateCustomer_thenReturnCustomer() {
        //given: the expected customer ID
        UUID expectedCustomerId = UUID.randomUUID();

        //and: mocking the save method to return a customer with this expected ID
        given(customerRepository.save(any(Customer.class))).willAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.setId(expectedCustomerId);
            return savedCustomer;
        });

        //when: calling the service
        UUID actualCustomerId = customerService.createCustomer(testApplicationUser);

        //then: the saving has been completed
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());

        //and: retrieve the captured customer
        Customer capturedCustomer = customerCaptor.getValue();

        //and: assert the customer's properties match the application user's properties
        assertNotNull(capturedCustomer);
        assertEquals(testApplicationUser.getEmail(), capturedCustomer.getUser().getEmail());
        assertEquals(testApplicationUser.getFirstName(), capturedCustomer.getUser().getFirstName());
        assertEquals(testApplicationUser.getLastName(), capturedCustomer.getUser().getLastName());
        assertEquals(testApplicationUser.getAddress(), capturedCustomer.getAddress());
        assertEquals(testApplicationUser.getAge(), capturedCustomer.getAge());

        //and: the returned customer ID matches the expected one
        assertEquals(expectedCustomerId, actualCustomerId);
    }

    @Test
    @DisplayName("Getting a customer by ID succeeds")
    public void givenCustomerId_whenGetCustomerById_thenReturnCustomer() {
        //given: a customer ID
        UUID customerId = UUID.randomUUID();

        //and: an expected customer
        Customer expectedCustomer = new Customer(testApplicationUser);
        expectedCustomer.setId(customerId);

        //and: mocking the repository to return this customer
        given(customerRepository.findById(customerId)).willReturn(Optional.of(expectedCustomer));

        //when: calling the service
        Customer savedCustomer = customerService.getCustomerById(expectedCustomer.getId());

        //then: the customer has been found
        verify(customerRepository).findById(customerId);
        assertThat(savedCustomer).isNotNull();
        assertEquals(expectedCustomer, savedCustomer);
    }

    @Test
    @DisplayName("Getting a customer by invalid ID fails")
    public void givenInvalidCustomerId_whenGetCustomerById_thenReturnCustomerNotFound() {
        //given: an invalid customer ID
        UUID invalidCustomerId = UUID.randomUUID();

        //and: mocking the repository to simulate that no customer exists with this ID
        given(customerRepository.findById(invalidCustomerId)).willReturn(Optional.empty());

        //when: calling the service we expect to receive the proper exception
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerById(invalidCustomerId));

        //then: we verify the repository interaction
        verify(customerRepository).findById(invalidCustomerId);
    }

    @Test
    @DisplayName("Update customer age and address but not the role, succeeds")
    public void givenCustomerObject_whenUpdateCustomer_thenReturnUpdatedCustomer() {
        //given: a random customer and an update request
        Customer customer = easyRandom.nextObject(Customer.class);
        UpdateCustomerRequest request = new UpdateCustomerRequest(39, "New Address", null);

        //and: mocking the service to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //when: updating the customer according to the request
        Customer updatedCustomer = customerService.updateCustomer(customer.getId(), request);

        //then: the customer's parameters have been updated successfully expect role
        assertThat(updatedCustomer.getAge()).isEqualTo(request.getAge());
        assertThat(updatedCustomer.getAddress()).isEqualTo(request.getAddress());
        assertThat(updatedCustomer.getUser().getRole()).isEqualTo(customer.getUser().getRole());
    }

    @Test
    @DisplayName("Update customer with same age, address and role fails")
    public void givenInvalidUpdateRequest_whenUpdateCustomer_thenReturnUpdatedCustomer() {
        //given: a random customer and an update request with same property values as customer's
        Customer customer = easyRandom.nextObject(Customer.class);
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                customer.getAge(),
                customer.getAddress(),
                customer.getUser().getRole());

        //and: mocking the service to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //when: trying to update the customer's properties, an exception is thrown
        assertThrows(RequestValidationException.class,
                () -> customerService.updateCustomer(customer.getId(), request));

        //then: no interactions with the repository take place
        verify(customerRepository, never()).save(customer);
    }
}
