package com.project.trackfit.trainer.service;

import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.user.component.Role;
import com.project.trackfit.core.exception.ResourceNotFoundException;
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
public class PersonalTrainerServiceTests {

    @Mock
    private PersonalTrainerRepository personalTrainerRepository;

    @InjectMocks
    private PersonalTrainerService personalTrainerService;

    private ApplicationUser testApplicationUser;

    @BeforeEach
    public void setUp() {
        testApplicationUser = new ApplicationUser (
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                Role.TRAINER,
                38,
                "Athens, Greece"
        );
    }

    @Test
    @DisplayName("Check createTrainer method with valid Email address")
    public void testCreateTrainer() {
        UUID expectedTrainerId = UUID.randomUUID();
        when(personalTrainerRepository.save(any(PersonalTrainer.class))).thenAnswer(invocation -> {
            PersonalTrainer savedTrainer = invocation.getArgument(0);
            savedTrainer.setId(expectedTrainerId);
            return savedTrainer;
        });

        UUID actualTrainerId = personalTrainerService.createTrainer(testApplicationUser);

        verify(personalTrainerRepository).save(any(PersonalTrainer.class));
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

        when(personalTrainerRepository.findById(customerId)).thenReturn(Optional.of(expectedPersonalTrainer));

        PersonalTrainer result = personalTrainerService.getTrainerByID(customerId);

        verify(personalTrainerRepository).findById(customerId);
        assertEquals(expectedPersonalTrainer, result);
    }

    @Test
    @DisplayName("Check getCustomerById method with invalid Id")
    public void testGetCustomerByIdWithInvalidId() {
        UUID invalidCustomerId = UUID.randomUUID();
        when(personalTrainerRepository.findById(invalidCustomerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            personalTrainerService.getTrainerByID(invalidCustomerId);
        });

        verify(personalTrainerRepository).findById(invalidCustomerId);
    }
}