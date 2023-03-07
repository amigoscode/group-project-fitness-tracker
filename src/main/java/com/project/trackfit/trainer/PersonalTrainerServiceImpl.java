package com.project.trackfit.trainer;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.registration.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private  final PersonalTrainerRepo personalTrainerRepo;
    private  final EmailValidator emailValidator;
    private  final TrainerRetrieveRequestMapper retrieveRequestMapper;

    private PersonalTrainer findOrThrow(final UUID id){
        return personalTrainerRepo.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }




    //TODO: Preform Create.
    @Override
    public PersonalTrainer createTrainer(PersonalTrainer personalTrainer) {
        checkEmailValidity(personalTrainer);
        checkEmailExists(personalTrainer.getEmail());
        return personalTrainerRepo.save(personalTrainer);
    }

    @Override
    public PersonalTrainer getTrainerByID(UUID id) {
        return findOrThrow(id);
    }

    @Override
    public Iterable<RetrieveTrainerRequest> findAllTrainers() {
        return personalTrainerRepo
                .findAll()
                .stream()
                .map(retrieveRequestMapper)
                .collect(Collectors.toList());
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
