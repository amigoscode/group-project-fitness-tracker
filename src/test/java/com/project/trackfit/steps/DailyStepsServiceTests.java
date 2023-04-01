package com.project.trackfit.steps;

import com.project.trackfit.core.exception.DailyStepsNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DailyStepsServiceTests {
    @InjectMocks
    private DailyStepsService dailyStepsService;

    @Mock
    private DailyStepsRepository dailyStepsRepository;

    @Mock
    private DailyStepsRetrieveRequestMapper dailyStepsRetrieveRequestMapper;

    @Mock
    private CustomerRepository customerRepository;

    private CreateDailyStepsRequest createDailyStepsRequest;
    private RetrieveDailyStepsRequest retrieveDailyStepsRequest;
    private UUID customerId;
    private UUID dailyStepsId;
    private Customer customer;
    private DailySteps dailySteps;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        dailyStepsId = UUID.randomUUID();
        customer = new Customer();
        dailySteps = new DailySteps();

        createDailyStepsRequest = new CreateDailyStepsRequest(
                "10000",
                LocalDateTime.now(),
                customerId,
                null
        );

        retrieveDailyStepsRequest = new RetrieveDailyStepsRequest(
                dailyStepsId,
                "10000",
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Should create daily steps with the given request")
    void testCreateDailySteps() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dailyStepsRepository.save(any(DailySteps.class))).thenReturn(dailySteps);

        dailyStepsService.createDailySteps(createDailyStepsRequest);

        verify(customerRepository).findById(customerId);
        verify(dailyStepsRepository).save(any(DailySteps.class));
    }

    @Test
    @DisplayName("Should return exception when customer doesn't exist")
    void testCreateDailySteps_customerNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(
                ResourceNotFoundException.class,
                () -> dailyStepsService.createDailySteps(createDailyStepsRequest)
        );
    }

    @Test
    @DisplayName("Should return the customer daily steps")
    void testGetCustomerDailySteps() {
        Set<DailySteps> dailyStepsSet = new HashSet<>();
        dailyStepsSet.add(dailySteps);
        customer.setSteps(dailyStepsSet);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dailyStepsRetrieveRequestMapper.apply(dailySteps)).thenReturn(retrieveDailyStepsRequest);

        List<RetrieveDailyStepsRequest> result = dailyStepsService.getCustomerDailySteps(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dailyStepsId, result.get(0).id());
        assertEquals("10000", result.get(0).steps());

        verify(customerRepository).findById(customerId);
        verify(dailyStepsRetrieveRequestMapper).apply(dailySteps);
    }

    @Test
    @DisplayName("Should throw exception when customer not found")
    void testGetCustomerDailySteps_customerNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> dailyStepsService.getCustomerDailySteps(customerId)
        );
    }

    @Test
    @DisplayName("Should retrieve daily steps by id")
    void testRetrieveDailyStepsById() {
        when(dailyStepsRepository.findById(dailyStepsId)).thenReturn(Optional.of(dailySteps));
        when(dailyStepsRetrieveRequestMapper.apply(dailySteps)).thenReturn(retrieveDailyStepsRequest);

        RetrieveDailyStepsRequest result = dailyStepsService.retrieveDailyStepsById(dailyStepsId);

        assertNotNull(result);
        assertEquals(dailyStepsId, result.id());
        assertEquals("10000", result.steps());

        verify(dailyStepsRepository).findById(dailyStepsId);
        verify(dailyStepsRetrieveRequestMapper).apply(dailySteps);
    }

    @Test
    @DisplayName("Should throw exception when daily steps not found")
    void testRetrieveDailyStepsById_notFound() {
        when(dailyStepsRepository.findById(dailyStepsId)).thenReturn(Optional.empty());

        assertThrows(
                DailyStepsNotFoundException.class,
                () -> dailyStepsService.retrieveDailyStepsById(dailyStepsId)
        );
    }
}
