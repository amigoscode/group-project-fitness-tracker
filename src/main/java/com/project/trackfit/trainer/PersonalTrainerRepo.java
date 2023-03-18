package com.project.trackfit.trainer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public interface PersonalTrainerRepo  extends JpaRepository<PersonalTrainer, UUID> {
        Optional<PersonalTrainer> findByUser_Email(String email);





        }
