package com.project.trackfit.customer;

import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.customer.service.CustomerService;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.user.component.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRetrieveRequestMapper customerRetrieveRequestMapper;

    @InjectMocks
    private CustomerService customerService;

    private ApplicationUser testApplicationUser;

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

        UUID actualCustomerId = customerService.createCustomer(testApplicationUser);

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
}