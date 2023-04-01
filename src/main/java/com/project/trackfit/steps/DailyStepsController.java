package com.project.trackfit.steps;

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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/dailysteps")
public class DailyStepsController {

    private final DailyStepsService dailyStepsService;


    /**
     * Spring Boot REST API creates Daily Steps
     * http://[::1]:8080/api/v1/dailysteps
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> createDailySteps(@Valid @RequestBody CreateDailyStepsRequest createDailyStepsRequest) {
        UUID dailyStepsId = dailyStepsService.createDailySteps(createDailyStepsRequest);
        Map<String, UUID> data = new HashMap<>();
        data.put("DailySteps_Id", dailyStepsId);
        return new ResponseEntity(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Daily Steps have been created successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build(),
                CREATED
        );
    }

    /**
     * Spring Boot REST API gets Daily Steps by Id
     * http://[::1]:8080/api/v1/dailysteps/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getDailyStepsById(@PathVariable("id") UUID dailyStepsId) {
        RetrieveDailyStepsRequest dailyStepsRequest = dailyStepsService.retrieveDailyStepsById(dailyStepsId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("dailySteps", dailyStepsRequest))
                        .message("Daily Steps have been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Spring Boot REST API gets all Daily Steps by customerId
     * http://[::1]:8080/api/v1/dailysteps/customer/{customerId}
     */
    @GetMapping("customer/{customerId}")
    public ResponseEntity<APICustomResponse> getCustomerDailySteps(@PathVariable("customerId") UUID customerId) {
        List<RetrieveDailyStepsRequest> customerDailySteps = dailyStepsService.getCustomerDailySteps(customerId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("customerDailySteps", customerDailySteps))
                        .message("Customer Daily Steps have been retrieved successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}