package com.project.trackfit.customer;

import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRetrieveRequestMapper customerRetrieveRequestMapper;

    @InjectMocks
    private ICustomerService customerService;

    private ApplicationUser testApplicationUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testApplicationUser = new ApplicationUser (
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[] {},
                new byte[] {},
                Role.CUSTOMER
        );
    }

    @Test
    @DisplayName("Check createCustomer method with valid Email address")
    public void testCreateCustomer() {
        UUID expectedCustomerId = UUID.randomUUID();
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.setId(expectedCustomerId);
            return savedCustomer;
        });

        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(
                "John",
                "Doe",
                30,
                "john.doe@example.com",
                "123 Main St",
                "securePassword"
        );

        UUID actualCustomerId = customerService.createCustomer(testApplicationUser, createCustomerRequest);

        verify(customerRepository).save(any(Customer.class));
        assertNotNull(actualCustomerId);
        assertEquals(expectedCustomerId, actualCustomerId);
    }

    @Test
    @DisplayName("Check getCustomerById method with valid Id")
    public void testGetCustomerByIdWithValidId() {
        UUID customerId = UUID.randomUUID();
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(customerId);
        expectedCustomer.setUser(testApplicationUser);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        Customer result = customerService.getCustomerById(customerId);

        verify(customerRepository).findById(customerId);
        assertEquals(expectedCustomer, result);
    }

    @Test
    @DisplayName("Check getCustomerById method with invalid Id")
    public void testGetCustomerByIdWithInvalidId() {
        UUID invalidCustomerId = UUID.randomUUID();
        when(customerRepository.findById(invalidCustomerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(invalidCustomerId);
        });

        verify(customerRepository).findById(invalidCustomerId);
    }

    @Test
    @DisplayName("Check RetrieveCustomerById method with valid Id")
    public void testRetrieveCustomerByIdWithValidId() {
        UUID customerId = UUID.randomUUID();
        ApplicationUser testApplicationUser = new ApplicationUser(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[]{},
                new byte[]{},
                Role.CUSTOMER
        );
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setUser(testApplicationUser);

        RetrieveCustomerRequest expectedRetrieveCustomerRequest = new RetrieveCustomerRequest(
                customerId,
                "Andreas",
                "Kreouzos",
                0,
                "andreas.kreouzos@hotmail.com",
                null
        );
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRetrieveRequestMapper.apply(customer)).thenReturn(expectedRetrieveCustomerRequest);

        RetrieveCustomerRequest result = customerService.RetrieveCustomerById(customerId);

        verify(customerRepository).findById(customerId);
        verify(customerRetrieveRequestMapper).apply(customer);
        assertEquals(expectedRetrieveCustomerRequest, result);
    }

    @Test
    @DisplayName("Check RetrieveCustomerById method with invalid Id")
    public void testRetrieveCustomerByIdWithInvalidId() {
        UUID invalidCustomerId = UUID.randomUUID();
        when(customerRepository.findById(invalidCustomerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.RetrieveCustomerById(invalidCustomerId);
        });

        verify(customerRepository).findById(invalidCustomerId);
    }
}