package com.project.trackfit.trainer;

import java.util.UUID;


public interface PersonalTrainerService {
    UUID createTrainer(CreateTrainerRequest createTrainerRequest);
    PersonalTrainer getTrainerByID(UUID trainer_id);
    RetrieveTrainerRequest retrieveTrainerByID(UUID trainer_id);
    Iterable<RetrieveTrainerRequest> findAllTrainers();
}
