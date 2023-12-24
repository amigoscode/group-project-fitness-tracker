package com.project.trackfit.trainer;

import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.trainer.dto.RetrieveTrainerRequest;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TrainerRetrieveRequestMapper implements Function<PersonalTrainer, RetrieveTrainerRequest> {
    @Override
    public RetrieveTrainerRequest apply(PersonalTrainer trainer) {
        return new RetrieveTrainerRequest(
                trainer.getId(),
                trainer.getUser().getEmail(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getPhoneNumber()

        );
    }
}