package com.project.trackfit.subscription;

import com.project.trackfit.core.model.CustomResponse;

import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerService;
import com.project.trackfit.trainer.PersonalTrainer;
import com.project.trackfit.trainer.PersonalTrainerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/subscription")
public class SubscriptionController {
    private  final SubscriptionService subscriptionService;

    private  final ModelMapper modelMapper;

    private CreateSubscriptionRequest convertToDto(Subscription entity){

        return  modelMapper.map(entity, CreateSubscriptionRequest.class);
    }
    private Subscription convertToEntity(CreateSubscriptionRequest dto){
        return  modelMapper.map(dto, Subscription.class);
    }
    @PostMapping
    public ResponseEntity<CustomResponse>subscribe(@Valid @RequestBody CreateSubscriptionRequest subscriptionRequest){

        subscriptionService.createSubscription(subscriptionRequest);

        return ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Subscription",""))
                        .message("Personal Trainer have been Created Successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );

    }
    @GetMapping
    public ResponseEntity<CustomResponse>getAllSubscriptions(){
        Iterable<RetrieveSubscriptionRequest>subscriptionRequests=subscriptionService.findAllSubscription();
        return   ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Subscription",subscriptionRequests
                        ))
                        .message("Fetched All Subscription")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }

    /*
        TODO:
        MAKE CALL TO FETCH THE AUTHENTICATED
        USER SUBSCRIPTIONS

     */
    @GetMapping("{subId}")
    public ResponseEntity<CustomResponse>getSubscriptionDetails(@PathVariable("subId")UUID subId){
        CreateSubscriptionRequest subscription=convertToDto(subscriptionService.findSubscriptionByID(subId));
        return   ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("Trainer",subscription))
                        .message("Fetched All Personal Trainers")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
