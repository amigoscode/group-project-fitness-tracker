package com.project.trackfit.trainer.service;

import com.project.trackfit.core.mapper.CommonMapper;
import com.project.trackfit.trainer.dto.TrainerResponse;
import com.project.trackfit.trainer.entity.PersonalTrainer;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import com.project.trackfit.user.entity.ApplicationUser;
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
    public UUID createTrainer(ApplicationUser applicationUser) {
        PersonalTrainer personalTrainer = new PersonalTrainer(applicationUser);
        personalTrainerRepository.save(personalTrainer);
        return personalTrainer.getId();
    }

    @Override
    public TrainerResponse getTrainerByID(UUID id) {
        PersonalTrainer trainer = personalTrainerRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return mapToTrainerResponse(trainer);
    }

    @Override
    public List<TrainerResponse> findAllTrainers() {
        List<PersonalTrainer> trainers = personalTrainerRepository.findAll();
        return trainers.stream()
                .map(CommonMapper::mapToTrainerResponse)
                .collect(Collectors.toList());
    }
}