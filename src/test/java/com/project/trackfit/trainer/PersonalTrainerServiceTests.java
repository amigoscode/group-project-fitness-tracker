package com.project.trackfit.trainer;

import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.core.Role;
import com.project.trackfit.core.exception.ResourceNotFoundException;
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

public class PersonalTrainerServiceTests {

    @Mock
    private PersonalTrainerRepo personalTrainerRepo;

    @Mock
    private TrainerRetrieveRequestMapper trainerRetrieveRequestMapper;

    @InjectMocks
    private PersonalTrainerService personalTrainerService;

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
                Role.TRAINER
        );
    }

    @Test
    @DisplayName("Check createTrainer method with valid Email address")
    public void testCreateTrainer() {
        UUID expectedTrainerId = UUID.randomUUID();
        when(personalTrainerRepo.save(any(PersonalTrainer.class))).thenAnswer(invocation -> {
            PersonalTrainer savedTrainer = invocation.getArgument(0);
            savedTrainer.setId(expectedTrainerId);
            return savedTrainer;
        });

        UUID actualTrainerId = personalTrainerService.createTrainer(testApplicationUser);

        verify(personalTrainerRepo).save(any(PersonalTrainer.class));
        assertNotNull(actualTrainerId);
        assertEquals(expectedTrainerId, actualTrainerId);
    }

    @Test
    @DisplayName("Check getCustomerById method with valid Id")
    public void testGetCustomerByIdWithValidId() {
        UUID customerId = UUID.randomUUID();
        PersonalTrainer expectedPersonalTrainer = new PersonalTrainer();
        expectedPersonalTrainer.setId(customerId);
        expectedPersonalTrainer.setUser(testApplicationUser);

        when(personalTrainerRepo.findById(customerId)).thenReturn(Optional.of(expectedPersonalTrainer));

        PersonalTrainer result = personalTrainerService.getTrainerByID(customerId);

        verify(personalTrainerRepo).findById(customerId);
        assertEquals(expectedPersonalTrainer, result);
    }

    @Test
    @DisplayName("Check getCustomerById method with invalid Id")
    public void testGetCustomerByIdWithInvalidId() {
        UUID invalidCustomerId = UUID.randomUUID();
        when(personalTrainerRepo.findById(invalidCustomerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            personalTrainerService.getTrainerByID(invalidCustomerId);
        });

        verify(personalTrainerRepo).findById(invalidCustomerId);
    }

    @Test
    @DisplayName("Check RetrieveCustomerById method with valid Id")
    public void testRetrieveCustomerByIdWithValidId() {
        UUID trainerId = UUID.randomUUID();
        ApplicationUser testApplicationUser = new ApplicationUser(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[]{},
                new byte[]{},
                Role.CUSTOMER
        );
        PersonalTrainer personalTrainer = new PersonalTrainer();
        personalTrainer.setId(trainerId);
        personalTrainer.setUser(testApplicationUser);

        RetrieveTrainerRequest expectedRetrieveTrainerRequest = new RetrieveTrainerRequest(
                trainerId,
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                "0"
        );
        when(personalTrainerRepo.findById(trainerId)).thenReturn(Optional.of(personalTrainer));
        when(trainerRetrieveRequestMapper.apply(personalTrainer)).thenReturn(expectedRetrieveTrainerRequest);

        RetrieveTrainerRequest result = personalTrainerService.retrieveTrainerByID(trainerId);

        verify(personalTrainerRepo).findById(trainerId);
        verify(trainerRetrieveRequestMapper).apply(personalTrainer);
        assertEquals(expectedRetrieveTrainerRequest, result);
    }

    @Test
    @DisplayName("Check RetrieveCustomerById method with invalid Id")
    public void testRetrieveCustomerByIdWithInvalidId() {
        UUID invalidTrainerId = UUID.randomUUID();
        when(personalTrainerRepo.findById(invalidTrainerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            personalTrainerService.retrieveTrainerByID(invalidTrainerId);
        });

        verify(personalTrainerRepo).findById(invalidTrainerId);
    }
}