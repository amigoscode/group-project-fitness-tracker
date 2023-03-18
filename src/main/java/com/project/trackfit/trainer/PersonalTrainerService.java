package com.project.trackfit.trainer;

import com.project.trackfit.core.ApplicationUser;

import java.util.UUID;


public interface PersonalTrainerService {
    UUID createTrainer(ApplicationUser applicationUser);
    PersonalTrainer getTrainerByID(UUID trainer_id);
    RetrieveTrainerRequest retrieveTrainerByID(UUID trainer_id);
    Iterable<RetrieveTrainerRequest> findAllTrainers();
}
