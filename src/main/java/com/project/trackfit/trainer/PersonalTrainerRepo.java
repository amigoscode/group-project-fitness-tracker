package com.project.trackfit.trainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalTrainerRepo  extends JpaRepository<PersonalTrainer, UUID> {
        Optional<PersonalTrainer> findByUser_Email(String email);
}