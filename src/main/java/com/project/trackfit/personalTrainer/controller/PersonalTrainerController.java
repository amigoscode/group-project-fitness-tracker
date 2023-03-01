package com.project.trackfit.personalTrainer.controller;
import com.project.trackfit.core.model.CustomResponse;
import com.project.trackfit.personalTrainer.model.PersonalTrainer;
import com.project.trackfit.personalTrainer.service.PersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/trainers")
public class PersonalTrainerController {
    private  final PersonalTrainerService personalTrainerService;

    @PostMapping
    public ResponseEntity<CustomResponse> createTrainer(@RequestBody PersonalTrainer personalTrainer){
        PersonalTrainer performCreate=personalTrainerService.createTrainer(personalTrainer);
        return  ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Trainer_ID",performCreate.getId()))
                        .message("Personal Trainer have been Created Successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }
    @GetMapping
    public ResponseEntity<CustomResponse>getAllTrainers(){
      return   ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Personal Trainers",personalTrainerService.findAllTrainers()))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }


    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {
        PersonalTrainer trainer = personalTrainerService.getTrainerByID(trainerId);
        return   ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Trainer",trainer))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}

