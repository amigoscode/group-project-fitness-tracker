package com.project.trackfit.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String jsonPayLoad = "{\"firstName\": \"Andreas\", \"lastName\": \"Kreouzos\", " +
            "\"age\": 37, \"email\": \"andreas.kreouzos@hotmail.com\", \"address\": \"Athens, Greece\"}";

    @Test
    @DisplayName("Should return the user which created through a post request")
    public void createUser() throws Exception {
        ResultActions resultActions = getResultActions();
        resultActions
                .andExpect(jsonPath("$.customer_id").value(1))
                .andExpect(jsonPath("$.firstName").value("Andreas"))
                .andExpect(jsonPath("$.lastName").value("Kreouzos"))
                .andExpect(jsonPath("$.age").value(37))
                .andExpect(jsonPath("$.email").value("andreas.kreouzos@hotmail.com"))
                .andExpect(jsonPath("$.address").value("Athens, Greece"));
    }

    @Test
    @DisplayName("Should return the user by Id through a get request")
    public void getUserById() throws Exception {
        getResultActions();

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customer_id").value(1))
                .andExpect(jsonPath("$.firstName").value("Andreas"))
                .andExpect(jsonPath("$.lastName").value("Kreouzos"))
                .andExpect(jsonPath("$.age").value(37))
                .andExpect(jsonPath("$.email").value("andreas.kreouzos@hotmail.com"))
                .andExpect(jsonPath("$.address").value("Athens, Greece"));
    }

    private ResultActions getResultActions() throws Exception {
        return mockMvc.perform(post("/api/v1/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayLoad))
                .andExpect(status().isCreated());
    }
}