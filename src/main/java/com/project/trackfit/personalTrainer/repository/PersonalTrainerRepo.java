package com.project.trackfit.personalTrainer.repository;

import com.project.trackfit.personalTrainer.model.PersonalTrainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonalTrainerRepo  extends JpaRepository<PersonalTrainer, UUID> {
        }
