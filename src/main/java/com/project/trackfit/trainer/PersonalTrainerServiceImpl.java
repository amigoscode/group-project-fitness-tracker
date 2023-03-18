package com.project.trackfit.trainer;

import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.registration.EmailValidator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private final PersonalTrainerRepo personalTrainerRepo;
    private final TrainerRetrieveRequestMapper retrieveRequestMapper;



    private PersonalTrainer findOrThrow(final UUID id) {
        return personalTrainerRepo.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public UUID createTrainer(ApplicationUser applicationUser) {
        //Add Personal Trainer
        PersonalTrainer personalTrainer = new PersonalTrainer(
              applicationUser
        );

        personalTrainerRepo.save(personalTrainer);
        return personalTrainer.getId();
    }

    @Override
    public PersonalTrainer getTrainerByID(UUID id) {
        return findOrThrow(id);
    }

    @Override
    public RetrieveTrainerRequest retrieveTrainerByID(UUID trainer_id) {
        return personalTrainerRepo.
                findById(trainer_id)
                .map(retrieveRequestMapper)
                .orElseThrow(ResourceNotFoundException::new);
        
    }

    @Override
    public Iterable<RetrieveTrainerRequest> findAllTrainers() {
        return personalTrainerRepo
                .findAll()
                .stream()
                .map(retrieveRequestMapper)
                .collect(Collectors.toList());
    }

}
