package com.project.trackfit.trainer;

import com.project.trackfit.core.APICustomResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/trainers")
@PreAuthorize("isAuthenticated()")
public class PersonalTrainerController {

    private final IPersonalTrainerService personalTrainerService;

    /**
     * Gets all Personal Trainers
     * http://[::1]:8080/api/v1/trainers
     */
    @GetMapping
    public ResponseEntity<APICustomResponse> getAllTrainers() {
        Iterable<RetrieveTrainerRequest> trainers = personalTrainerService.findAllTrainers();

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Personal Trainers", trainers))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    /**
     * Gets a Trainer by Id
     * http://[::1]:8080/api/v1/trainers/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {
        RetrieveTrainerRequest trainer = personalTrainerService.retrieveTrainerByID(trainerId);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("trainer", trainer))
                        .message("Trainer has been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }
}
