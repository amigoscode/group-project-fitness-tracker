package com.project.trackfit.trainer.service;

import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PersonalTrainerService implements IPersonalTrainerService {

    private final PersonalTrainerRepository personalTrainerRepository;

    public PersonalTrainerService(PersonalTrainerRepository personalTrainerRepository) {
        this.personalTrainerRepository = personalTrainerRepository;
    }

    @Override
    public UUID createTrainer(ApplicationUser applicationUser) {
        PersonalTrainer personalTrainer = new PersonalTrainer(applicationUser);
        personalTrainerRepository.save(personalTrainer);
        return personalTrainer.getId();
    }

    @Override
    public PersonalTrainer getTrainerByID(UUID id) {
        return personalTrainerRepository.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<PersonalTrainer> findAllTrainers() {
        return personalTrainerRepository.findAll();
    }
}