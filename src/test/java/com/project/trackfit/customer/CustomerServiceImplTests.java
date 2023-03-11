package com.project.trackfit.customer;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.validation.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmailValidator emailValidator;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testCustomer = new Customer(
                UUID.randomUUID(),
                "Andreas",
                "Kreouzos",
                37,
                "andreas.kreouzos@hotmail.com",
                "Athens, Greece"
        );
    }

    @Test
    @DisplayName("Check createCustomer method with valid Email address")
    public void testCreateCustomerWithValidEmail() {
        when(emailValidator.checkMailPattern(anyString())).thenReturn(true);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        Customer createdCustomer = customerService.createCustomer(testCustomer);

        verify(emailValidator).checkMailPattern(anyString());
        verify(customerRepository).existsByEmail(anyString());
        verify(customerRepository).save(any(Customer.class));
        assert(createdCustomer).equals(testCustomer);
    }

    @Test
    @DisplayName("Check createCustomer method with invalid Email address")
    public void testCreateCustomerWithInvalidEmail() {
        when(emailValidator.checkMailPattern(anyString())).thenReturn(false);

        assertThrows(EmailNotValidException.class, () -> {
            customerService.createCustomer(testCustomer);
        });

        verify(emailValidator).checkMailPattern(anyString());
        verify(customerRepository, never()).existsByEmail(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Check createCustomer method with already taken Email address")
    public void testCreateCustomerWithEmailAlreadyTaken() {
        when(emailValidator.checkMailPattern(anyString())).thenReturn(true);
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyTakenException.class, () -> {
            customerService.createCustomer(testCustomer);
        });

        verify(emailValidator).checkMailPattern(anyString());
        verify(customerRepository).existsByEmail(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Check getCustomerById method with valid Id")
    public void testGetCustomerByIdWithValidId() {
        UUID testCustomerId = testCustomer.getId();
        when(customerRepository.findById(testCustomerId)).thenReturn(Optional.of(testCustomer));

        Customer retrievedCustomer = customerService.getCustomerById(testCustomerId);

        verify(customerRepository).findById(testCustomerId);
        assert(retrievedCustomer).equals(testCustomer);
    }

    @Test
    @DisplayName("Check createCustomer method with invalid Id")
    public void testGetCustomerByIdWithInvalidId() {
        UUID invalidCustomerId = UUID.randomUUID();
        when(customerRepository.findById(invalidCustomerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(invalidCustomerId);
        });

        verify(customerRepository).findById(invalidCustomerId);
    }
}
