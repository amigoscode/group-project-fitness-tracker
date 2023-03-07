package com.project.trackfit.trainer;
import com.project.trackfit.core.model.CustomResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

    private RetrieveTrainerRequest convertToDto(PersonalTrainer entity)
    {
     return modelMapper.map(entity,RetrieveTrainerRequest.class);
    }
    private PersonalTrainer convertToEntity(PersonalTrainerDTO dto)
    {
        return modelMapper.map(dto, PersonalTrainer.class);
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
        Iterable<RetrieveTrainerRequest> trainers = personalTrainerService.findAllTrainers();

        return   ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Personal Trainers",trainers
                                ))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }


    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {

        RetrieveTrainerRequest trainer = convertToDto(personalTrainerService.getTrainerByID(trainerId));

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

