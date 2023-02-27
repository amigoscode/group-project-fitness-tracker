package com.project.trackfit.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String jsonPayLoad = "{\"firstName\": \"Andreas\", \"lastName\": \"Kreouzos\", " +
            "\"age\": 37, \"email\": \"andreas.kreouzos@hotmail.com\", \"address\": \"Athens, Greece\"}";

    @Test
    @DisplayName("Should return the user which created through a post request")
    public void createUser() throws Exception {
        mockMvc.perform(post("/api/v1/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayLoad))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customer_id").value(1))
                .andExpect(jsonPath("$.firstName").value("Andreas"))
                .andExpect(jsonPath("$.lastName").value("Kreouzos"))
                .andExpect(jsonPath("$.age").value(37))
                .andExpect(jsonPath("$.email").value("andreas.kreouzos@hotmail.com"))
                .andExpect(jsonPath("$.address").value("Athens, Greece"));
    }
}