package com.project.trackfit.trainer.service;

import com.project.trackfit.trainer.dto.TrainerResponse;
import com.project.trackfit.user.entity.ApplicationUser;

import java.util.List;
import java.util.UUID;


public interface IPersonalTrainerService {
    UUID createTrainer(ApplicationUser applicationUser);
    TrainerResponse getTrainerByID(UUID trainer_id);
    List<TrainerResponse> findAllTrainers();
}