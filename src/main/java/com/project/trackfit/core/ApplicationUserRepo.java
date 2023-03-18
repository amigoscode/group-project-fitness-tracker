package com.project.trackfit.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser>findByEmail(String email);
    boolean existsByEmail(String email);
}
