package com.project.trackfit.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

}
