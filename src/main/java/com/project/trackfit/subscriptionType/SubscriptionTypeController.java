package com.project.trackfit.subscriptionType;

import com.project.trackfit.core.APICustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/subscribeType")
public class SubscriptionTypeController {
    final private ISubscriptionTypeService subscriptionTypeService;
    @PostMapping
    public ResponseEntity<APICustomResponse>createSubscribeType(@Valid @RequestBody CreateSubscriptionTypeRequest createSubscriptionTypeRequest){
        UUID subscriptionTypeId=subscriptionTypeService.createSubscriptionType(createSubscriptionTypeRequest);
        Map<String, UUID> data = new HashMap<>();
        data.put("subscriptionTypeId", subscriptionTypeId);

        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Subscription Type have been Created Successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<APICustomResponse>getSubTypeById(@PathVariable("subTypeId") UUID subTypeId){
        RetrieveSubscriptionTypeRequest retrieveSubscriptionTypeRequest=subscriptionTypeService.getSubscriptionTypeById(subTypeId);
        Map<String, RetrieveSubscriptionTypeRequest> data = new HashMap<>();
        data.put("SubscriptionTypeDetails", retrieveSubscriptionTypeRequest);

        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Fetched Subscription Type Details")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }


}
