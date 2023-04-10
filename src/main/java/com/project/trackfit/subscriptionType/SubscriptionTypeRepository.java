package com.project.trackfit.subscriptionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, UUID> {
}
