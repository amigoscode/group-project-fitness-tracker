package com.project.trackfit.steps;

import com.project.trackfit.core.exception.DailyStepsNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
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

    private DailyStepsRequest dailyStepsRequest;
    private DailyStepsResponse dailyStepsResponse;
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

        dailyStepsRequest = new DailyStepsRequest(
                "10000",
                LocalDateTime.now(),
                customerId,
                null
        );

        dailyStepsResponse = new DailyStepsResponse(
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

        dailyStepsService.createDailySteps(dailyStepsRequest);

        verify(customerRepository).findById(customerId);
        verify(dailyStepsRepository).save(any(DailySteps.class));
    }

    @Test
    @DisplayName("Should return exception when customer doesn't exist")
    void testCreateDailySteps_customerNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(
                ResourceNotFoundException.class,
                () -> dailyStepsService.createDailySteps(dailyStepsRequest)
        );
    }

    @Test
    @DisplayName("Should retrieve daily steps by id")
    void testRetrieveDailyStepsById() {
        when(dailyStepsRepository.findById(dailyStepsId)).thenReturn(Optional.of(dailySteps));
        when(dailyStepsRetrieveRequestMapper.apply(dailySteps)).thenReturn(dailyStepsResponse);

        DailyStepsResponse result = dailyStepsService.retrieveDailyStepsById(dailyStepsId);

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
