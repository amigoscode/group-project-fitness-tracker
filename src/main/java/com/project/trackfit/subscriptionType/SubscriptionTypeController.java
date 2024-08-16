package com.project.trackfit.subscriptionType;

import com.project.trackfit.core.APICustomResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/subscribeType")
public class SubscriptionTypeController {

    private final ISubscriptionTypeService subscriptionTypeService;

    public SubscriptionTypeController(ISubscriptionTypeService subscriptionTypeService) {
        this.subscriptionTypeService = subscriptionTypeService;
    }

    @PostMapping
    public ResponseEntity<APICustomResponse> createSubscribeType(@Valid @RequestBody SubscriptionTypeRequest subscriptionTypeRequest){
        UUID subscriptionTypeId = subscriptionTypeService.createSubscriptionType(subscriptionTypeRequest);

        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("subscriptionTypeId", subscriptionTypeId))
                        .message("Subscription Type have been Created Successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("{subTypeId}")
    public ResponseEntity<APICustomResponse> getSubTypeById(@PathVariable("subTypeId") UUID subTypeId){
        SubscriptionTypeResponse subscriptionType = subscriptionTypeService.getSubscriptionTypeById(subTypeId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("SubscriptionTypeDetails", subscriptionType))
                        .message("Fetched Subscription Type Details")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
}
