package com.project.trackfit.subscription;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.core.GenericController;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/subscription")
public class SubscriptionController extends GenericController {

    private final ISubscriptionService ISubscriptionService;

    /**
     * Creates Subscriptions
     * http://[::1]:8080/api/v1/subscription
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> subscribe(
            @Valid @RequestBody CreateSubscriptionRequest subscriptionRequest) {
        UUID subscriptionId = ISubscriptionService.createSubscription(subscriptionRequest);
        return createResponse(
                Map.of("SubscriptionId", subscriptionId),
                "Personal Trainer have been Created Successfully",
                CREATED);
    }

    /**
     * Gets all Subscriptions
     * http://[::1]:8080/api/v1/subscription
     */
    @GetMapping
    public ResponseEntity<APICustomResponse> getAllSubscriptions() {
        Iterable<RetrieveSubscriptionRequest> subscriptionRequests = ISubscriptionService.findAllSubscription();
        return createResponse(
                Map.of("Subscription", subscriptionRequests),
                "Fetched All Subscription",
                OK);
    }

    /**
     * Gets all Subscriptions by ID
     * http://[::1]:8080/api/v1/subscription/{subId}
     */
    @GetMapping("{subId}")
    public ResponseEntity<APICustomResponse> getSubscriptionDetails(
            @PathVariable("subId") UUID subId) {
        RetrieveSubscriptionRequest subscriptionDetails = ISubscriptionService.findSubscriptionByID(subId);
        return createResponse(
                Map.of("SubscriptionDetails", subscriptionDetails),
                "Fetched All Personal Trainers",
                OK);
    }
}
