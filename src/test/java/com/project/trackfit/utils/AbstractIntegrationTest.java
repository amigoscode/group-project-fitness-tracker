package com.project.trackfit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.user.repository.ApplicationUserRepo;
import com.project.trackfit.user.component.Role;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private ApplicationUserRepo applicationUserRepo;

    protected HttpHeaders headers;

    @BeforeEach
    protected void abstractSetUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    protected void registerUser(String email, String firstName, String lastName, Integer password, Role role, Integer age, String address) {
        String registerUrl = "http://localhost:" + port + "/api/v1/auth/register";
        HttpEntity<String> registerRequest = new HttpEntity<>(String.format("{\"email\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"password\":\"%s\",\"role\":\"%s\",\"age\":\"%s\",\"address\":\"%s\"}", email, firstName, lastName, password, role.name(), age, address), headers);
        restTemplate.postForEntity(registerUrl, registerRequest, String.class);
    }

    protected String getJwtToken(String email, Integer password) throws JsonProcessingException {
        String tokenUrl = "http://localhost:" + port + "/api/v1/auth/token";
        HttpEntity<String> tokenRequest = new HttpEntity<>(String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password), headers);
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, String.class);

        System.out.println("Response body: " + tokenResponse.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(tokenResponse.getBody());
        System.out.println(jsonNode);
        return jsonNode.get("data").get("accessToken").asText();
    }
}