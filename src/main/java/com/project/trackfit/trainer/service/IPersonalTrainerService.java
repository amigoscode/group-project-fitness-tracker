package com.project.trackfit.trainer.service;

import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.user.dto.ApplicationUser;

import java.util.List;
import java.util.UUID;


public interface IPersonalTrainerService {
    UUID createTrainer(ApplicationUser applicationUser);
    PersonalTrainer getTrainerByID(UUID trainer_id);
    List<PersonalTrainer> findAllTrainers();
}