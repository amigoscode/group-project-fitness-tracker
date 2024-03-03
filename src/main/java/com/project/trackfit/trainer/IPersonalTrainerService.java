package com.project.trackfit.trainer;

import com.project.trackfit.trainer.PersonalTrainerResponse;
import com.project.trackfit.user.User;

import java.util.List;
import java.util.UUID;


public interface IPersonalTrainerService {
    UUID createTrainer(User user);
    PersonalTrainerResponse getTrainerByID(UUID trainer_id);
    List<PersonalTrainerResponse> findAllTrainers();
}