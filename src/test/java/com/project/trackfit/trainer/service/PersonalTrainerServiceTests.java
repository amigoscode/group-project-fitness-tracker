package com.project.trackfit.trainer.service;

import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.trainer.entity.PersonalTrainer;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import com.project.trackfit.user.entity.ApplicationUser;
import com.project.trackfit.user.component.Role;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonalTrainerServiceTests {

    @Mock
    private PersonalTrainerRepository personalTrainerRepository;

    @InjectMocks
    private PersonalTrainerService personalTrainerService;

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
                Role.TRAINER,
                38,
                "Athens, Greece",
                "00306931234567"
        );
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(FieldPredicates.named("age")
                                .and(FieldPredicates.inClass(Customer.class)
                                        .or(FieldPredicates.inClass(ApplicationUser.class))),
                        () -> new Random().nextInt(83) + 18);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("Successfully create a personal trainer")
    public void givenUUID_whenCreateTrainer_thenReturnTrainer() {
        //given: the expected trainer ID
        UUID expectedTrainerId = UUID.randomUUID();

        //and: mocking the save method to return a trainer with this expected ID
        given(personalTrainerRepository.save(any(PersonalTrainer.class))).willAnswer(invocation -> {
            PersonalTrainer savedTrainer = invocation.getArgument(0);
            savedTrainer.setId(expectedTrainerId);
            return savedTrainer;
        });

        //when: calling the service
        UUID actualTrainerId = personalTrainerService.createTrainer(testApplicationUser);

        //then: the saving has been completed
        ArgumentCaptor<PersonalTrainer> trainerCaptor = ArgumentCaptor.forClass(PersonalTrainer.class);
        verify(personalTrainerRepository).save(trainerCaptor.capture());

        //and: retrieve the captured trainer
        PersonalTrainer capturedTrainer = trainerCaptor.getValue();

        //and: assert the trainer's properties match the application user's properties
        assertNotNull(capturedTrainer);
        assertEquals(testApplicationUser.getEmail(), capturedTrainer.getUser().getEmail());
        assertEquals(testApplicationUser.getFirstName(), capturedTrainer.getUser().getFirstName());
        assertEquals(testApplicationUser.getLastName(), capturedTrainer.getUser().getLastName());
        assertEquals(testApplicationUser.getAddress(), capturedTrainer.getUser().getAddress());
        assertEquals(testApplicationUser.getAge(), capturedTrainer.getUser().getAge());

        //and: the returned trainer ID matches the expected one
        assertEquals(expectedTrainerId, actualTrainerId);
    }

    @Test
    @DisplayName("Getting a trainer by ID succeeds")
    public void givenTrainerId_whenGetTrainerById_thenReturnTrainer() {
        //given: a trainer ID
        UUID trainerId = UUID.randomUUID();

        //and: an expected trainer
        PersonalTrainer expectedTrainer = new PersonalTrainer(testApplicationUser);
        expectedTrainer.setId(trainerId);

        //and: mocking the repository to return this trainer
        given(personalTrainerRepository.findById(trainerId)).willReturn(Optional.of(expectedTrainer));

        //when: calling the service
        PersonalTrainer savedTrainer = personalTrainerService.getTrainerByID(trainerId);

        //then: the trainer has been found
        verify(personalTrainerRepository).findById(trainerId);
        assertThat(savedTrainer).isNotNull();
        assertEquals(expectedTrainer, savedTrainer);
    }

    @Test
    @DisplayName("Getting a trainer by invalid ID fails")
    public void givenInvalidTrainerId_whenGetTrainerById_thenReturnTrainerNotFound() {
        //given: an invalid trainer ID
        UUID invalidTrainerId = UUID.randomUUID();

        //and: mocking the repository to simulate that no trainer exists with this ID
        given(personalTrainerRepository.findById(invalidTrainerId)).willReturn(Optional.empty());

        //when: calling the service we expect to receive the proper exception
        assertThrows(ResourceNotFoundException.class,
                () -> personalTrainerService.getTrainerByID(invalidTrainerId));

        //then: we verify the repository interaction
        verify(personalTrainerRepository).findById(invalidTrainerId);
    }

    @Test
    @DisplayName("Getting a list of trainers")
    public void givenTrainers_whenFindAllTrainers_thenReturnListOfTrainers() {
        //given: two random trainers
        PersonalTrainer firstTrainer = easyRandom.nextObject(PersonalTrainer.class);
        PersonalTrainer secondTrainer = easyRandom.nextObject(PersonalTrainer.class);

        //and: mocking the repository to simulate the return of them in a list
        given(personalTrainerRepository.findAll()).willReturn(List.of(firstTrainer, secondTrainer));

        //when: calling the service
        List<PersonalTrainer> trainersList = personalTrainerService.findAllTrainers();

        //then: the list is not null and contains these two trainers
        assertThat(trainersList).isNotNull();
        assertThat(trainersList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Getting an empty list of trainers")
    public void givenNoTrainers_whenFindAllTrainers_thenReturnEmptyList() {
        //given: the mocking of the repository to simulate the return of an empty list
        given(personalTrainerRepository.findAll()).willReturn(Collections.emptyList());

        //when: calling the service
        List<PersonalTrainer> trainersList = personalTrainerService.findAllTrainers();

        //then: the list is indeed empty
        assertThat(trainersList).isEmpty();
        assertThat(trainersList.size()).isEqualTo(0);
    }
}