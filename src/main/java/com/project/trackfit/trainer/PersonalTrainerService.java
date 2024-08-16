package com.project.trackfit.trainer;

import com.project.trackfit.core.mapper.CommonMapper;
import com.project.trackfit.user.User;
import com.project.trackfit.core.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.project.trackfit.core.mapper.CommonMapper.mapToTrainerResponse;

@Service
public class PersonalTrainerService implements IPersonalTrainerService {

    private final PersonalTrainerRepository personalTrainerRepository;

    public PersonalTrainerService(PersonalTrainerRepository personalTrainerRepository) {
        this.personalTrainerRepository = personalTrainerRepository;
    }

    @Override
    public UUID createTrainer(User user) {
        PersonalTrainer personalTrainer = new PersonalTrainer(user);
        personalTrainerRepository.save(personalTrainer);
        return personalTrainer.getId();
    }

    @Override
    public PersonalTrainerResponse getTrainerByID(UUID id) {
        PersonalTrainer trainer = personalTrainerRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return mapToTrainerResponse(trainer);
    }

    @Override
    public List<PersonalTrainerResponse> findAllTrainers() {
        List<PersonalTrainer> trainers = personalTrainerRepository.findAll();
        return trainers.stream()
                .map(CommonMapper::mapToTrainerResponse)
                .collect(Collectors.toList());
    }
}