package com.project.trackfit.personalTrainer.service.impl;

import com.project.trackfit.exception.EmailAlreadyTakenException;
import com.project.trackfit.exception.EmailNotValidException;
import com.project.trackfit.exception.ResourceNotFoundException;
import com.project.trackfit.personalTrainer.model.entity.PersonalTrainerEntity;
import com.project.trackfit.personalTrainer.repository.PersonalTrainerRepo;
import com.project.trackfit.personalTrainer.service.PersonalTrainerService;
import com.project.trackfit.registration.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private  final PersonalTrainerRepo personalTrainerRepo;
    private  final EmailValidator emailValidator;

    private PersonalTrainerEntity findOrThrow(final UUID id){
        return personalTrainerRepo.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }




    //TODO: Preform Create.
    @Override
    public PersonalTrainerEntity createTrainer(PersonalTrainerEntity personalTrainerEntity) {
        checkEmailValidity(personalTrainerEntity);
        checkEmailExists(personalTrainerEntity.getEmail());
        return personalTrainerRepo.save(personalTrainerEntity);
    }

    @Override
    public PersonalTrainerEntity getTrainerByID(UUID id) {
        return findOrThrow(id);
    }

    @Override
    public Iterable<PersonalTrainerEntity> findAllTrainers() {
        return personalTrainerRepo.findAll();
    }

    private void checkEmailValidity(PersonalTrainerEntity trainer) {
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
