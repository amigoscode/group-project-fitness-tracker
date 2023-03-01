package com.project.trackfit.personalTrainer.controller;

import com.project.trackfit.model.User;
import com.project.trackfit.personalTrainer.model.PersonalTrainer;
import com.project.trackfit.personalTrainer.service.PersonalTrainerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/trainers")
public class PersonalTrainerController {
    private  final PersonalTrainerService personalTrainerService;

    @PostMapping
    public ResponseEntity<PersonalTrainer> createTrainer(@RequestBody PersonalTrainer personalTrainer){
        PersonalTrainer performCreate=personalTrainerService.createTrainer(personalTrainer);
        return new ResponseEntity<>(performCreate, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<Iterable<PersonalTrainer>>getAllTrainers(){
        return new ResponseEntity<>(personalTrainerService.findAllTrainers(),HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<PersonalTrainer> getTrainerById(@PathVariable("id") UUID trainerId) {
        PersonalTrainer trainer = personalTrainerService.getTrainerByID(trainerId);
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }
}

