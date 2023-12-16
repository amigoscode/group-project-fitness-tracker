package com.project.trackfit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.security.jwt.JwtRequestFilter;
import com.project.trackfit.user.controller.ApplicationUserController;
import com.project.trackfit.user.dto.CreateUserRequest;
import com.project.trackfit.user.service.IApplicationUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;
import java.util.stream.Stream;

import static com.project.trackfit.core.Role.CUSTOMER;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@WebMvcTest(
        controllers = ApplicationUserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtRequestFilter.class
                ),
        })
public class ApplicationUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IApplicationUserService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Successfully create a new user for the application")
    public void givenUserRequest_whenCreateUser_thenReturnSavedUser() throws Exception {
        //given: the request to create a user
        CreateUserRequest request = new CreateUserRequest(
                "andreas.kreouzos@hotmail.com",
                "MyPassword",
                "Andreas",
                "Kreouzos",
                CUSTOMER,
                38,
                "Athens, Greece"
        );

        //and: mocking the service to return a UUID
        given(service.createUser(any(CreateUserRequest.class))).willAnswer((invocation) -> UUID.randomUUID());

        //when: sending the request
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then: the proper response is expected
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Application user has been created successfully")))
                .andExpect(jsonPath("$.statusCode", is(201)))
                .andExpect(jsonPath("$.data.User_Id", notNullValue()))
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @ParameterizedTest
    @MethodSource("wrongInputParameters")
    @DisplayName("Creating a new user fails due to wrong request parameters")
    public void givenWrongParameters_whenCreateUser_thenUserNotCreated(CreateUserRequest userInput, String expectedEmailError) throws Exception {
        //given: the request to create a user with empty and null values
        CreateUserRequest request = new CreateUserRequest(
                userInput.getEmail(),
                userInput.getPassword(),
                userInput.getFirstName(),
                userInput.getLastName(),
                userInput.getRole(),
                userInput.getAge(),
                userInput.getAddress());

        //and: mocking the service to return a UUID
        given(service.createUser(any(CreateUserRequest.class))).willAnswer((invocation) -> UUID.randomUUID());

        //when: sending the request
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then: the proper error validation messages are expected
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", containsInAnyOrder(expectedEmailError)))
                .andExpect(jsonPath("$.password", containsInAnyOrder("Password is required")))
                .andExpect(jsonPath("$.firstName", containsInAnyOrder("First Name is required")))
                .andExpect(jsonPath("$.lastName", containsInAnyOrder("Last Name is required")))
                .andExpect(jsonPath("$.role", containsInAnyOrder("Role cannot be null")))
                .andExpect(jsonPath("$.age", containsInAnyOrder("Age cannot be null")))
                .andExpect(jsonPath("$.address", containsInAnyOrder("Address is required")));
    }

    static Stream<Arguments> wrongInputParameters() {
        return Stream.of(
                Arguments.of(new CreateUserRequest("", "", "", "", null, null, ""), "Email must not be blank"),
                Arguments.of(new CreateUserRequest("invalid_email", "", "", "", null, null, ""), "Email is invalid")
        );
    }
}
