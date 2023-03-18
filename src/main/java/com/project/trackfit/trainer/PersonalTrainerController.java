package com.project.trackfit.trainer;

import com.project.trackfit.core.APICustomResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/trainers")
@PreAuthorize("isAuthenticated()")
public class PersonalTrainerController {
    private final PersonalTrainerService personalTrainerService;

    @GetMapping
    public ResponseEntity<APICustomResponse> getAllTrainers() {
        Iterable<RetrieveTrainerRequest> trainers = personalTrainerService.findAllTrainers();
        Map<String, Iterable<RetrieveTrainerRequest>> data = new HashMap<>();
        data.put("Personal Trainers", trainers);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }

    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {
        RetrieveTrainerRequest trainer = personalTrainerService.retrieveTrainerByID(trainerId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(trainer)
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}