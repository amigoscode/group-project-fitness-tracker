package com.project.trackfit.steps;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DailyStepsRepository extends JpaRepository<DailySteps, UUID> {;
}