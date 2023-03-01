package com.project.trackfit.personalTrainer.service;

import com.project.trackfit.personalTrainer.model.PersonalTrainer;

import java.util.UUID;


public interface PersonalTrainerService {
    PersonalTrainer createTrainer(PersonalTrainer personalTrainer);
    PersonalTrainer getTrainerByID(UUID trainer_id);

    Iterable<PersonalTrainer> findAllTrainers();

}
