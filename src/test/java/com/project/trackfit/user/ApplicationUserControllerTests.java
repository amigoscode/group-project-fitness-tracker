package com.project.trackfit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.core.IApplicationUserService;
import com.project.trackfit.core.configuration.SecurityConfig;
import com.project.trackfit.security.jwt.ApplicationConfig;
import com.project.trackfit.security.jwt.JwtService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static com.project.trackfit.core.Role.CUSTOMER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ApplicationUserController.class)
@Import(SecurityConfig.class)
public class ApplicationUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IApplicationUserService service;

/*    @MockBean
    private ApplicationConfig applicationConfig;

    @MockBean
    private JwtService jwtService;*/

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Test the controller create user")
    @Test
    @WithMockUser
    public void givenUserRequest_whenCreateUser_thenReturnSavedUser() throws Exception {
        //given - the request to create a user
        CreateUserRequest request = new CreateUserRequest(
                "andreas.kreouzos@hotmail.com",
                "MyPassword",
                "Andreas",
                "Kreouzos",
                CUSTOMER,
                38,
                "Athens, Greece"
        );

        given(service.createUser(any(CreateUserRequest.class))).willAnswer((invocation) -> UUID.randomUUID());

/*        ApplicationUser dummyUser = new ApplicationUser();
        given(applicationConfig.authenticate(contains("andreas.kreouzos@hotmail.com"), contains("MyPassword"))).willReturn(dummyUser);

        given(jwtService.validateToken(anyString(), any(UserDetails.class))).willReturn(true);
        given(jwtService.extractUsername(anyString())).willReturn("andreas.kreouzos@hotmail.com");*/

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", CoreMatchers.is("Application user has been created successfully")));
    }
}
