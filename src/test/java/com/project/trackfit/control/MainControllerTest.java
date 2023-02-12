package com.project.trackfit.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Testing the getCustomer()")
    public void testLoginPage() throws Exception{
        this.mockMvc.perform(get("/customers/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_id").value(1))
                .andExpect(jsonPath("$.firstName").value("Andreas"))
                .andExpect(jsonPath("$.lastName").value("Kreouzos"))
                .andExpect(jsonPath("$.age").value(37))
                .andExpect(jsonPath("$.email").value("andreas.kreouzos@email.com"))
                .andExpect(jsonPath("$.address").value("Athens, Greece"));
    }
}
