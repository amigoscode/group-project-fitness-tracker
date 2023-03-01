package com.project.trackfit.personalTrainer.service.impl;

import com.project.trackfit.personalTrainer.model.PersonalTrainer;
import com.project.trackfit.personalTrainer.repository.PersonalTrainerRepo;
import com.project.trackfit.personalTrainer.service.PersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {
    @Autowired
    private  final PersonalTrainerRepo personalTrainerRepo;


    @Override
    public PersonalTrainer createTrainer(PersonalTrainer personalTrainer) {
        return null;
    }

    @Override
    public PersonalTrainer getTrainerByID(PersonalTrainer personalTrainer) {
        return null;
    }
}
