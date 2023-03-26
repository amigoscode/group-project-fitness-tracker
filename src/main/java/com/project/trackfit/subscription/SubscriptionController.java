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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/subscription")
public class SubscriptionController {

    private final ISubscriptionService ISubscriptionService;

    @PostMapping
    public ResponseEntity<APICustomResponse> subscribe(@Valid @RequestBody CreateSubscriptionRequest subscriptionRequest) {
        UUID subscriptionId = ISubscriptionService.createSubscription(subscriptionRequest);
        Map<String, UUID> data = new HashMap<>();
        data.put("SubscriptionId", subscriptionId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Personal Trainer have been Created Successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );

    }

    @GetMapping
    public ResponseEntity<APICustomResponse> getAllSubscriptions() {
        Iterable<RetrieveSubscriptionRequest> subscriptionRequests = ISubscriptionService.findAllSubscription();
        Map<String, Iterable<RetrieveSubscriptionRequest>> data = new HashMap<>();
        data.put("Subscription", subscriptionRequests);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Fetched All Subscription")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }

    @GetMapping("{subId}")
    public ResponseEntity<APICustomResponse> getSubscriptionDetails(@PathVariable("subId") UUID subId) {
        RetrieveSubscriptionRequest subscriptionDetails = ISubscriptionService.findSubscriptionByID(subId);
        Map<String, RetrieveSubscriptionRequest> data = new HashMap<>();
        data.put("SubscriptionDetails", subscriptionDetails);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("SubscriptionDetails", subscriptionDetails))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}