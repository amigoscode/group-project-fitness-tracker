package com.project.trackfit.trainer;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.validation.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private final PersonalTrainerRepo personalTrainerRepo;
    private final EmailValidator emailValidator;
    private final TrainerRetrieveRequestMapper retrieveRequestMapper;

    private PersonalTrainer findOrThrow(final UUID id) {
        return personalTrainerRepo.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public UUID createTrainer(CreateTrainerRequest createTrainerRequest) {
        checkEmailValidity(createTrainerRequest);
        checkEmailExists(createTrainerRequest.email());
        PersonalTrainer personalTrainer = new PersonalTrainer(
                createTrainerRequest.email(),
                createTrainerRequest.firstName(),
                createTrainerRequest.lastName(),
                createTrainerRequest.phoneNumber()
        );

        personalTrainerRepo.save(personalTrainer);
        return personalTrainer.getId();
    }

    @Override
    public PersonalTrainer getTrainerByID(UUID id) {
        return findOrThrow(id);
    }

    @Override
    public RetrieveTrainerRequest retrieveTrainerByID(UUID trainer_id) {
        return personalTrainerRepo.
                findById(trainer_id)
                .map(retrieveRequestMapper)
                .orElseThrow(ResourceNotFoundException::new);
        
    }

    @Override
    public Iterable<RetrieveTrainerRequest> findAllTrainers() {
        return personalTrainerRepo
                .findAll()
                .stream()
                .map(retrieveRequestMapper)
                .collect(Collectors.toList());
    }

    private void checkEmailValidity(CreateTrainerRequest trainer) {
        if (!(emailValidator.checkMailPattern(trainer.email()))) {
            throw new EmailNotValidException();
        }
    }

    private void checkEmailExists(String email) {
        if (personalTrainerRepo.existsByEmail(email)) {
            throw new EmailAlreadyTakenException();
        }
    }
}
