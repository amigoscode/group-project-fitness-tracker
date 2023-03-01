package com.project.trackfit.personalTrainer.service;

import com.project.trackfit.personalTrainer.model.PersonalTrainer;


public interface PersonalTrainerService {
    PersonalTrainer createTrainer(PersonalTrainer personalTrainer);
    PersonalTrainer getTrainerByID(PersonalTrainer personalTrainer);

}
