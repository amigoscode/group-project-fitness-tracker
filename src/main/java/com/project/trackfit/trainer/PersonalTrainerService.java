package com.project.trackfit.trainer;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public interface PersonalTrainerService {
    UUID createTrainer(CreateTrainerRequest createTrainerRequest) throws NoSuchAlgorithmException;

    PersonalTrainer getTrainerByID(UUID trainer_id);

    RetrieveTrainerRequest retrieveTrainerByID(UUID trainer_id);


    Iterable<RetrieveTrainerRequest> findAllTrainers();


}
