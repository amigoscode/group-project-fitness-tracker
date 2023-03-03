package com.project.trackfit.personalTrainer.repository;

import com.project.trackfit.personalTrainer.model.entity.PersonalTrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonalTrainerRepo  extends JpaRepository<PersonalTrainerEntity, UUID> {
        boolean existsByEmail(String email);
        }
