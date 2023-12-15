package com.project.trackfit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.core.IApplicationUserService;
import com.project.trackfit.security.jwt.JwtRequestFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static com.project.trackfit.core.Role.CUSTOMER;
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

    @DisplayName("Successfully create a new user for the application")
    @Test
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
}
