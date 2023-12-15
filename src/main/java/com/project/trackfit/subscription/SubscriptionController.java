package com.project.trackfit.subscription;

import com.project.trackfit.core.APICustomResponse;
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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/subscription")
public class SubscriptionController {

    private final ISubscriptionService service;

    /**
     * Creates Subscriptions
     * http://[::1]:8080/api/v1/subscription
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> subscribe(
            @Valid @RequestBody CreateSubscriptionRequest subscriptionRequest) {
        UUID subscriptionId = service.createSubscription(subscriptionRequest);

        return ResponseEntity.status(CREATED)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("SubscriptionId", subscriptionId))
                        .message("Subscription added Successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
                );
    }

    /**
     * Gets all Subscriptions
     * http://[::1]:8080/api/v1/subscription
     */
    @GetMapping
    public ResponseEntity<APICustomResponse> getAllSubscriptions() {
        Iterable<RetrieveSubscriptionRequest> subscriptionRequests = service.findAllSubscription();

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Subscription", subscriptionRequests))
                        .message("Fetched all subscriptions")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    /**
     * Gets all Subscriptions by ID
     * http://[::1]:8080/api/v1/subscription/{subId}
     */
    @GetMapping("{subId}")
    public ResponseEntity<APICustomResponse> getSubscriptionDetails(
            @PathVariable("subId") UUID subId) {
        RetrieveSubscriptionRequest subscriptionDetails = service.findSubscriptionByID(subId);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("SubscriptionDetails", subscriptionDetails))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }
}
