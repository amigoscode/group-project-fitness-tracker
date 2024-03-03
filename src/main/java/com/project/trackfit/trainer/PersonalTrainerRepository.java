package com.project.trackfit.trainer;

import com.project.trackfit.trainer.PersonalTrainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonalTrainerRepository extends JpaRepository<PersonalTrainer, UUID> {
}