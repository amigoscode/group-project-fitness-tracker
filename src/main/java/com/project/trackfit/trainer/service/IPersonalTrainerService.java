package com.project.trackfit.trainer.service;

import com.project.trackfit.trainer.entity.PersonalTrainer;
import com.project.trackfit.user.entity.ApplicationUser;

import java.util.List;
import java.util.UUID;


public interface IPersonalTrainerService {
    UUID createTrainer(ApplicationUser applicationUser);
    PersonalTrainer getTrainerByID(UUID trainer_id);
    List<PersonalTrainer> findAllTrainers();
}