package com.project.trackfit.trainer;

import com.project.trackfit.core.exception.EmailAlreadyTakenException;
import com.project.trackfit.core.exception.EmailNotValidException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.core.registration.EmailValidator;
import com.project.trackfit.core.util.SaltHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private final PersonalTrainerRepo personalTrainerRepo;
    private final EmailValidator emailValidator;
    private final TrainerRetrieveRequestMapper retrieveRequestMapper;
    private byte [] createSalt(){
        var random = new SecureRandom();
        var salt =new byte[128];
        random.nextBytes(salt);
        return salt;

    }

    private byte[] createPasswordHash(String password , byte[]salt) throws NoSuchAlgorithmException{
        var md= MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
    }


    private PersonalTrainer findOrThrow(final UUID id) {
        return personalTrainerRepo.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }


    //TODO: Preform Create.
    @Override
    public UUID createTrainer(CreateTrainerRequest createTrainerRequest) throws NoSuchAlgorithmException {
        checkEmailValidity(createTrainerRequest);
        checkEmailExists(createTrainerRequest.email());
        if(createTrainerRequest.password().isBlank()) throw new IllegalArgumentException(
                "Password is required"
        );
        byte[] salt= createSalt();
        byte[] hashedPassword=
                createPasswordHash(createTrainerRequest.password(), salt);

        //Add Personal Trainer
        PersonalTrainer personalTrainer = new PersonalTrainer(
                createTrainerRequest.email(),
                createTrainerRequest.firstName(),
                createTrainerRequest.lastName(),
                createTrainerRequest.phoneNumber(),
                salt,
                hashedPassword

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
