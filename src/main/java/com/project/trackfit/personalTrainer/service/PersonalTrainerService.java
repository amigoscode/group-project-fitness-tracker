package com.project.trackfit.personalTrainer.service;

import com.project.trackfit.personalTrainer.model.entity.PersonalTrainerEntity;

import java.util.UUID;


public interface PersonalTrainerService {
    PersonalTrainerEntity createTrainer(PersonalTrainerEntity personalTrainerEntity);
    PersonalTrainerEntity getTrainerByID(UUID trainer_id);

    Iterable<PersonalTrainerEntity> findAllTrainers();

}
