package com.project.trackfit.trainer;

import com.project.trackfit.core.APICustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/trainers")
public class PersonalTrainerController {

    private final IPersonalTrainerService personalTrainerService;

    public PersonalTrainerController(IPersonalTrainerService personalTrainerService) {
        this.personalTrainerService = personalTrainerService;
    }

    @GetMapping
    public ResponseEntity<APICustomResponse> getAllTrainers() {
        List<PersonalTrainerResponse> trainers = personalTrainerService.findAllTrainers();
        String message = trainers.isEmpty() ? "No trainers available" : "Fetched all personal trainers";

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Trainers", trainers))
                        .message(message)
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {
        PersonalTrainerResponse trainerRequest = personalTrainerService.getTrainerByID(trainerId);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Trainer", trainerRequest))
                        .message("Trainer has been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }
}
