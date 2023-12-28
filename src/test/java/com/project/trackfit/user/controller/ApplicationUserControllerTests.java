package com.project.trackfit.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.security.jwt.JwtRequestFilter;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static com.project.trackfit.user.component.Role.CUSTOMER;
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
    public void givenWrongParameters_whenCreateUser_thenUserNotCreated(String jsonRequest,
                                                                       String emailError,
                                                                       String ageError,
                                                                       String roleError) throws Exception {
        //when: sending the request
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        //then: the proper error validation messages are expected
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password[0]", is("Password is required")))
                .andExpect(jsonPath("$.firstName[0]", is("First Name is required")))
                .andExpect(jsonPath("$.lastName[0]", is("Last Name is required")))
                .andExpect(jsonPath("$.address[0]", is("Address is required")))
                .andExpect(jsonPath("$.email[0]", is(emailError)))
                .andExpect(jsonPath("$.role[0]", is(roleError)))
                .andExpect(jsonPath("$.age[0]", is(ageError)));
    }

    @ParameterizedTest
    @MethodSource("invalidRoleParameter")
    @DisplayName("Creating a new user fails due to invalid role")
    public void givenInvalidRole_whenCreateUser_thenBadRequest(String jsonRequest) throws Exception {
        //when: sending the request
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        //then: expect a general bad request error
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Bad Request")))
                .andExpect(jsonPath("$.details", is("Invalid value: 'INVALID' for the field: 'role'. The value must be one of: [CUSTOMER, TRAINER].")));
    }

    static Stream<Arguments> wrongInputParameters() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> firstInvalidMap = new HashMap<>();
        firstInvalidMap.put("email", "");
        firstInvalidMap.put("password", "");
        firstInvalidMap.put("firstName", "");
        firstInvalidMap.put("lastName", "");
        firstInvalidMap.put("role", null);
        firstInvalidMap.put("age", -18);
        firstInvalidMap.put("address", "");
        String firstInvalidJson = objectMapper.writeValueAsString(firstInvalidMap);

        Map<String, Object> secondInvalidMap = new HashMap<>();
        secondInvalidMap.put("email", "invalid_email");
        secondInvalidMap.put("password", "");
        secondInvalidMap.put("firstName", "");
        secondInvalidMap.put("lastName", "");
        secondInvalidMap.put("role", null);
        secondInvalidMap.put("age", 101);
        secondInvalidMap.put("address", "");
        String secondInvalidJson = objectMapper.writeValueAsString(secondInvalidMap);

        return Stream.of(
                Arguments.of(firstInvalidJson,
                        "Email must not be blank",
                        "Age must be at least 18",
                        "Role cannot be null"
                ),
                Arguments.of(
                        secondInvalidJson,
                        "Email is invalid",
                        "Age must be no more than 100",
                        "Role cannot be null"
                )
        );
    }

    static Stream<Arguments> invalidRoleParameter() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> invalidRoleMap = new HashMap<>();
        invalidRoleMap.put("email", "valid_email@example.com");
        invalidRoleMap.put("password", "ValidPass123");
        invalidRoleMap.put("firstName", "John");
        invalidRoleMap.put("lastName", "Doe");
        invalidRoleMap.put("role", "INVALID");
        invalidRoleMap.put("age", 25);
        invalidRoleMap.put("address", "123 Main St");
        String json1 = objectMapper.writeValueAsString(invalidRoleMap);
        return Stream.of(Arguments.of(json1));
    }
}
