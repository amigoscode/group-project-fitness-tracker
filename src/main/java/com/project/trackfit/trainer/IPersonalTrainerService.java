package com.project.trackfit.trainer;

import com.project.trackfit.user.ApplicationUser;

import java.util.UUID;


public interface IPersonalTrainerService {
    UUID createTrainer(ApplicationUser applicationUser);
    PersonalTrainer getTrainerByID(UUID trainer_id);
    RetrieveTrainerRequest retrieveTrainerByID(UUID trainer_id);
    Iterable<RetrieveTrainerRequest> findAllTrainers();
}