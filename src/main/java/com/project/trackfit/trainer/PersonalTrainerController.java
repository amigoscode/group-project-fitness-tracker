package com.project.trackfit.trainer;

import com.project.trackfit.core.model.APICustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/trainers")
public class PersonalTrainerController {
    private final PersonalTrainerService personalTrainerService;

    @PostMapping
    public ResponseEntity<APICustomResponse> createTrainer(@Valid @RequestBody CreateTrainerRequest createTrainerRequest) {
        UUID trainerId = personalTrainerService.createTrainer(createTrainerRequest);
        Map<String, UUID> data = new HashMap<>();
        data.put("Trainer_ID", trainerId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Personal Trainer have been Created Successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

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