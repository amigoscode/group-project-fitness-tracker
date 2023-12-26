package integration.com.project.trackfit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.Main;
import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.trainer.dto.PersonalTrainer;
import com.project.trackfit.trainer.repository.PersonalTrainerRepository;
import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.user.repository.ApplicationUserRepo;
import com.project.trackfit.user.component.Role;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonalTrainerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PersonalTrainerRepository personalTrainerRepository;

    @Autowired
    private ApplicationUserRepo applicationUserRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID trainerId;
    private HttpHeaders headers;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        trainerId = UUID.randomUUID();

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
    }

    private UUID createTrainer(UUID applicationUserId) {
        PersonalTrainer trainer = new PersonalTrainer();
        trainer.setId(UUID.randomUUID());

        ApplicationUser applicationUser = applicationUserRepo
                .findById(applicationUserId)
                .orElseThrow(() -> new IllegalStateException("User not found in the database"));
        trainer.setUser(applicationUser);

        PersonalTrainer savedTrainer = personalTrainerRepository.save(trainer);
        return savedTrainer.getId();
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