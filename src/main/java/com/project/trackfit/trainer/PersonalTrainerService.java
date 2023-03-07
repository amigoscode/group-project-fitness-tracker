package com.project.trackfit.trainer;

import java.util.UUID;


public interface PersonalTrainerService {
    PersonalTrainer createTrainer(PersonalTrainer personalTrainer);
    PersonalTrainer getTrainerByID(UUID trainer_id);

    Iterable<RetrieveTrainerRequest> findAllTrainers();


}
