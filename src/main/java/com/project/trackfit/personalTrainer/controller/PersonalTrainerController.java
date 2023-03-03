package com.project.trackfit.personalTrainer.controller;
import com.project.trackfit.core.model.CustomResponse;
import com.project.trackfit.personalTrainer.model.dto.PersonalTrainerDTO;
import com.project.trackfit.personalTrainer.model.entity.PersonalTrainerEntity;
import com.project.trackfit.personalTrainer.service.PersonalTrainerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/trainers")
public class PersonalTrainerController {
    private  final PersonalTrainerService personalTrainerService;
    private  final ModelMapper modelMapper;

    private PersonalTrainerDTO convertToDto(PersonalTrainerEntity entity)
    {
     return modelMapper.map(entity,PersonalTrainerDTO.class);
    }
    private PersonalTrainerEntity convertToEntity(PersonalTrainerDTO dto)
    {
        return modelMapper.map(dto,PersonalTrainerEntity.class);
    }
    @PostMapping
    public ResponseEntity<CustomResponse> createTrainer(@Valid @RequestBody PersonalTrainerDTO personalTrainerDTO){

        var entity = convertToEntity(personalTrainerDTO);
        var trainer= personalTrainerService.createTrainer(entity);
        return  ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Trainer_ID",trainer.getId()))
                        .message("Personal Trainer have been Created Successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }
    @GetMapping
    public ResponseEntity<CustomResponse>getAllTrainers(){
        List<PersonalTrainerEntity> trainerEntityList = StreamSupport
                .stream(personalTrainerService.findAllTrainers().spliterator(), false)
                .collect(Collectors.toList());

        return   ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Personal Trainers",trainerEntityList
                                .stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }


    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {

        PersonalTrainerDTO trainer = convertToDto(personalTrainerService.getTrainerByID(trainerId));

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

