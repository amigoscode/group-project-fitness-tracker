package com.project.trackfit.steps;

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
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/dailysteps")
public class DailyStepsController {

    private final DailyStepsService dailyStepsService;

    public DailyStepsController(DailyStepsService dailyStepsService) {
        this.dailyStepsService = dailyStepsService;
    }

    @PostMapping
    public ResponseEntity<APICustomResponse> createDailySteps(
            @Valid @RequestBody DailyStepsRequest dailyStepsRequest) {
        UUID dailyStepsId = dailyStepsService.createDailySteps(dailyStepsRequest);

        return ResponseEntity.status(CREATED)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("DailySteps_Id", dailyStepsId))
                        .message("Daily Steps have been created successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
                );
    }

    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getDailyStepsById(
            @PathVariable("id") UUID dailyStepsId) {
        DailyStepsResponse dailyStepsRequest = dailyStepsService.retrieveDailyStepsById(dailyStepsId);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("dailySteps", dailyStepsRequest))
                        .message("Daily Steps have been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }
}
