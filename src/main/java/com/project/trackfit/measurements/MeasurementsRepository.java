package com.project.trackfit.measurements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurements, UUID> {;
}