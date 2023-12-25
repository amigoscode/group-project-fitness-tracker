package com.project.trackfit.trainer.controller;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.trainer.service.IPersonalTrainerService;
import com.project.trackfit.trainer.service.PersonalTrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.project.trackfit.user.dto.ApplicationUser.mapData;
import static org.springframework.http.HttpStatus.OK;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/trainers")
public class PersonalTrainerController {

    private final IPersonalTrainerService personalTrainerService;

    public PersonalTrainerController(PersonalTrainerService personalTrainerService) {
        this.personalTrainerService = personalTrainerService;
    }

    @GetMapping
    public ResponseEntity<APICustomResponse> getAllTrainers() {
        List<PersonalTrainer> trainers = personalTrainerService.findAllTrainers();

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(convertTo(trainers))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {
        PersonalTrainer trainerRequest = personalTrainerService.getTrainerByID(trainerId);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(mapPersonalTrainerData(trainerRequest))
                        .message("Trainer has been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    public List<Map<String, Object>> convertTo(List<PersonalTrainer> trainers) {
        List<Map<String, Object>> trainersData = new ArrayList<>();
        for (PersonalTrainer trainer : trainers) {
            trainersData.add(mapPersonalTrainerData(trainer));
        }
        return trainersData;
    }

    public Map<String, Object> mapPersonalTrainerData(PersonalTrainer trainerRequest) {
        return mapData(trainerRequest.getId(), trainerRequest.getUser());
    }
}
