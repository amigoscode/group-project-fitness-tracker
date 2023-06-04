package com.project.trackfit.steps;

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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/dailysteps")
public class DailyStepsController extends GenericController {

    private final DailyStepsService dailyStepsService;


    /**
     * Creates Daily Steps
     * http://[::1]:8080/api/v1/dailysteps
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> createDailySteps(
            @Valid @RequestBody CreateDailyStepsRequest createDailyStepsRequest) {
        UUID dailyStepsId = dailyStepsService.createDailySteps(createDailyStepsRequest);
        return createResponse(
                Map.of("DailySteps_Id", dailyStepsId),
                "Daily Steps have been created successfully",
                CREATED);
    }

    /**
     * Gets Daily Steps by Id
     * http://[::1]:8080/api/v1/dailysteps/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getDailyStepsById(
            @PathVariable("id") UUID dailyStepsId) {
        RetrieveDailyStepsRequest dailyStepsRequest = dailyStepsService.retrieveDailyStepsById(dailyStepsId);
        return createResponse(
                Map.of("dailySteps", dailyStepsRequest),
                "Daily Steps have been fetched successfully",
                OK);
    }

}
