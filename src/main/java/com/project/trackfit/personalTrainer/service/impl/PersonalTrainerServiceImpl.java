package com.project.trackfit.personalTrainer.service.impl;

import com.project.trackfit.exception.EmailAlreadyTakenException;
import com.project.trackfit.exception.EmailNotValidException;
import com.project.trackfit.exception.ResourceNotFoundException;
import com.project.trackfit.model.User;
import com.project.trackfit.personalTrainer.model.PersonalTrainer;
import com.project.trackfit.personalTrainer.repository.PersonalTrainerRepo;
import com.project.trackfit.personalTrainer.service.PersonalTrainerService;
import com.project.trackfit.registration.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private  final PersonalTrainerRepo personalTrainerRepo;
    private  final EmailValidator emailValidator;




    //TODO: Preform Create.
    @Override
    public PersonalTrainer createTrainer(PersonalTrainer personalTrainer) {
        checkEmailValidity(personalTrainer);
        checkEmailExists(personalTrainer.getEmail());
        return personalTrainerRepo.save(personalTrainer);
    }

    @Override
    public PersonalTrainer getTrainerByID(UUID id) {
        PersonalTrainer trainer = personalTrainerRepo
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return trainer;
    }

    @Override
    public Iterable<PersonalTrainer> findAllTrainers() {
        return personalTrainerRepo.findAll();
    }

    private void checkEmailValidity(PersonalTrainer trainer) {
        if (!(emailValidator.checkMailPattern(trainer.getEmail()))){
            throw new EmailNotValidException();
        }
    }

    private void checkEmailExists(String email){
        if (personalTrainerRepo.existsByEmail(email)){
            throw new EmailAlreadyTakenException();
        }
    }
}
