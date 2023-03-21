package com.project.trackfit.trainer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.core.ApplicationUserRepo;
import com.project.trackfit.core.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PersonalTrainerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PersonalTrainerRepo personalTrainerRepo;

    @Autowired
    private ApplicationUserRepo applicationUserRepo;

    private HttpHeaders headers;

    private UUID trainerId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        trainerId = UUID.randomUUID();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String email = "email@example.com";
        String firstName = "Andreas";
        String lastName = "Kreouzos";
        Integer password = 123;
        Role role = Role.TRAINER;
        Integer age = 37;
        String address = "Athens";

        registerUser(email, firstName, lastName, password, role, age, address);
        String jwtToken = getJwtToken(email, password);

        headers.setBearerAuth(jwtToken);

        ApplicationUser applicationUser = applicationUserRepo
                .findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found in the database"));

        trainerId = createTrainer(applicationUser.getId());
    }

    @Test
    public void testGetUserById() {
        ResponseEntity<APICustomResponse> response = restTemplate
                .exchange("http://localhost:" + port + "/api/v1/trainers/" + trainerId,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        APICustomResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getStatusCode(), HttpStatus.OK.value());
    }

    private void registerUser(String email, String firstName, String lastName, Integer password, Role role, Integer age, String address) {
        String registerUrl = "http://localhost:" + port + "/api/v1/auth/register";
        HttpEntity<String> registerRequest = new HttpEntity<>(String.format("{\"email\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"password\":\"%s\",\"role\":\"%s\",\"age\":\"%s\",\"address\":\"%s\"}", email, firstName, lastName, password, role.name(), age, address), headers);
        restTemplate.postForEntity(registerUrl, registerRequest, String.class);
    }

    private String getJwtToken(String email, Integer password) throws JsonProcessingException {
        String tokenUrl = "http://localhost:" + port + "/api/v1/auth/token";
        HttpEntity<String> tokenRequest = new HttpEntity<>(String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password), headers);
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, String.class);

        System.out.println("Response body: " + tokenResponse.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(tokenResponse.getBody());
        return jsonNode.get("token").asText();
    }

    private UUID createTrainer(UUID applicationUserId) {
        PersonalTrainer trainer = new PersonalTrainer();
        trainer.setId(UUID.randomUUID());

        ApplicationUser applicationUser = applicationUserRepo
                .findById(applicationUserId)
                .orElseThrow(() -> new IllegalStateException("User not found in the database"));
        trainer.setUser(applicationUser);

        PersonalTrainer savedTrainer = personalTrainerRepo.save(trainer);
        return savedTrainer.getId();
    }
}