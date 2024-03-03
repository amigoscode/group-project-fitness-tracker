package com.project.trackfit.subscription;

import com.project.trackfit.core.APICustomResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/subscription")
public class SubscriptionController {

    private final ISubscriptionService service;

    public SubscriptionController(ISubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<APICustomResponse> subscribe(
            @Valid @RequestBody SubscriptionRequest subscriptionRequest) {
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

    @GetMapping
    public ResponseEntity<APICustomResponse> getAllSubscriptions() {
        List<SubscriptionResponse> subscriptionRequests = service.findAllSubscription();
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

    @GetMapping("{subId}")
    public ResponseEntity<APICustomResponse> getSubscriptionDetails(@PathVariable("subId") UUID subId) {
        SubscriptionResponse subscriptionDetails = service.findSubscriptionByID(subId);

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
