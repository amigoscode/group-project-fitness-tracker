package com.project.trackfit.trainer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.user.ApplicationUser;
import com.project.trackfit.user.ApplicationUserRepo;
import com.project.trackfit.core.Role;
import com.project.trackfit.utils.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PersonalTrainerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PersonalTrainerRepo personalTrainerRepo;

    @Autowired
    private ApplicationUserRepo applicationUserRepo;

    private UUID trainerId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        super.abstractSetUp();

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
        assertEquals(response.getBody().getStatusCode(), HttpStatus.OK.value());
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