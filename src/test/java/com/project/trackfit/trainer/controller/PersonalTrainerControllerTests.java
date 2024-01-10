package com.project.trackfit.trainer.controller;

import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.security.jwt.JwtRequestFilter;
import com.project.trackfit.trainer.entity.PersonalTrainer;
import com.project.trackfit.trainer.service.IPersonalTrainerService;
import com.project.trackfit.user.entity.ApplicationUser;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PersonalTrainerController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtRequestFilter.class
                ),
        })
public class PersonalTrainerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPersonalTrainerService service;

    private EasyRandom easyRandom;

    @BeforeEach
    public void setup() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(FieldPredicates.named("age")
                                .and(FieldPredicates.inClass(Customer.class)
                                        .or(FieldPredicates.inClass(ApplicationUser.class))),
                        () -> new Random().nextInt(83) + 18);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("Successfully get a trainer by his unique Id")
    public void givenTrainer_whenGetTrainerById_thenReturnTrainer() throws Exception {
        //given: a random personal trainer
        PersonalTrainer randomTrainer = easyRandom.nextObject(PersonalTrainer.class);

        //and: mocking the service to create a user with is UUID
        //given(userService.createUser(any(CreateUserRequest.class))).willAnswer((invocation) -> randomTrainer.getId());

        //and: mocking the service to return this random personal trainer
        given(service.getTrainerByID(randomTrainer.getId())).willReturn(randomTrainer);

        //when: trying to fetch this random personal trainer
        ResultActions response = mockMvc.perform(get("/api/v1/trainers/{id}", randomTrainer.getId()));

        //then: the response is OK with expected values
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Trainer has been fetched successfully")))
                .andExpect(jsonPath("$.data", notNullValue()));
    }

    @Test
    @DisplayName("Getting a trainer fails because he doesn't exist")
    public void givenNonExistingTrainerUUID_whenGetTrainerById_thenReturnIsNotFound() throws Exception {
        //given: a non-existent trainer id
        UUID nonExistentTrainerId = UUID.randomUUID();

        //and: mocking the service to return the proper exception
        given(service.getTrainerByID(nonExistentTrainerId)).willThrow(new ResourceNotFoundException());

        //when: trying to fetch this trainer
        ResultActions response = mockMvc.perform(get("/api/v1/trainers/{id}", nonExistentTrainerId));

        //then: the response is Not Found
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User Doesn't Exist")));
    }

    @Test
    @DisplayName("Successfully get a list of trainers")
    public void givenTrainers_whenGetTrainers_thenReturnListOfTrainers() throws Exception {
        //given: a list of personal trainers
        List<PersonalTrainer> trainers = new ArrayList<>();

        //and: a pair of random trainers
        PersonalTrainer firstTrainer = easyRandom.nextObject(PersonalTrainer.class);
        PersonalTrainer secondTrainer = easyRandom.nextObject(PersonalTrainer.class);

        //and: adding these trainers into this list
        trainers.add(firstTrainer);
        trainers.add(secondTrainer);

        //and: mocking the service to return this list
        given(service.findAllTrainers()).willReturn(trainers);

        //when: trying to fetch these personal trainers
        ResultActions response = mockMvc.perform(get("/api/v1/trainers"));

        //then: the response is OK with expected values
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Fetched all personal trainers")))
                .andExpect(jsonPath("$.data", notNullValue()));
    }

    @Test
    @DisplayName("Get an empty list of trainers succeeds with correct message")
    public void givenNoTrainers_whenGetTrainers_thenReturnEmptyList() throws Exception {
        //when: sending a request to the endpoint
        ResultActions response = mockMvc.perform(get("/api/v1/trainers"));

        //then: the response is OK but with a message indicating no trainers are available
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("No trainers available")))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}
