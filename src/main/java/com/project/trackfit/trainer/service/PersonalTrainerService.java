package com.project.trackfit.trainer.service;

import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public PersonalTrainer getTrainerByID(UUID id) {
        return personalTrainerRepository.
                findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<PersonalTrainer> findAllTrainers() {
        return personalTrainerRepository.findAll();
    }

    //TODO: Investigate the creation of an abstract class for common functionalities between this class and CustomerService
    public List<Map<String, Object>> mapPersonalTrainerDataList(List<PersonalTrainer> trainers) {
        List<Map<String, Object>> trainersData = new ArrayList<>();
        for (PersonalTrainer trainer : trainers) {
            trainersData.add(mapPersonalTrainerData(trainer));
        }
        return trainersData;
    }

    public Map<String, Object> mapPersonalTrainerData(PersonalTrainer trainerRequest) {
        Map<String, Object> trainerData = new HashMap<>();
        trainerData.put("id", trainerRequest.getId());
        trainerData.put("firstName", trainerRequest.getUser().getFirstName());
        trainerData.put("lastName", trainerRequest.getUser().getLastName());
        trainerData.put("age", trainerRequest.getUser().getAge());
        trainerData.put("email", trainerRequest.getUser().getEmail());
        trainerData.put("address", trainerRequest.getUser().getAddress());
        return trainerData;
    }
}