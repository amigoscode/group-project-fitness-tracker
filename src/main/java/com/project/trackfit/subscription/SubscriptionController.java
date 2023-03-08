package com.project.trackfit.subscription;

import com.project.trackfit.core.model.APICustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<APICustomResponse> subscribe(@Valid @RequestBody CreateSubscriptionRequest subscriptionRequest) {

        UUID subscriptionId = subscriptionService.createSubscription(subscriptionRequest);
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
        Iterable<RetrieveSubscriptionRequest> subscriptionRequests = subscriptionService.findAllSubscription();
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

    /*
        TODO:
        MAKE CALL TO FETCH THE AUTHENTICATED
        USER SUBSCRIPTIONS

     */
    @GetMapping("{subId}")
    public ResponseEntity<APICustomResponse> getSubscriptionDetails(@PathVariable("subId") UUID subId) {
        RetrieveSubscriptionRequest subscriptionDetails = subscriptionService.findSubscriptionByID(subId);
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
