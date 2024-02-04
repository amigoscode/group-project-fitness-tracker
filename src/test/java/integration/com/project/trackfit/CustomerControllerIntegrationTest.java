package integration.com.project.trackfit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.Main;
import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.user.dto.CreateUserRequest;
import com.project.trackfit.user.entity.ApplicationUser;
import com.project.trackfit.user.repository.ApplicationUserRepo;
import com.project.trackfit.user.component.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ApplicationUserRepo applicationUserRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID customerId;
    private HttpHeaders headers;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        customerId = UUID.randomUUID();

        CreateUserRequest request = new CreateUserRequest("email@example.com", "123", "Andreas", "Kreouzos", Role.CUSTOMER, 37, "Athens", "00306931234567");

        registerUser(request);
        String jwtToken = getJwtToken(request);

        headers.setBearerAuth(jwtToken);

        ApplicationUser applicationUser = applicationUserRepo
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found in the database"));

        customerId = createCustomer(applicationUser.getId());
    }

    @Test
    public void getCustomerById() {
        ResponseEntity<APICustomResponse> response = restTemplate
                .exchange("http://localhost:" + port + "/api/v1/customers/" + customerId,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        APICustomResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Successfully upload an image to the AWS S3 Bucket")
    public void canUploadImage() {
        //given: an image located in resources folder
        Resource image = new ClassPathResource("male.jpg");

        //and: create a MultiValueMap for this image
        LinkedMultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("image", image);

        //and: create the headers for the request
        HttpHeaders multipartHeaders = new HttpHeaders(headers);
        multipartHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        //and: create a new HttpEntity that carries the image and the headers
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(bodyMap, multipartHeaders);

        //when: calling the controller
        ResponseEntity<APICustomResponse> response = restTemplate
                .exchange("http://localhost:" + port + "/api/v1/customers/" + customerId + "/upload",
                        HttpMethod.POST,
                        requestEntity,
                        APICustomResponse.class);

        //then: the response is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private UUID createCustomer(UUID applicationUserId) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());

        ApplicationUser applicationUser = applicationUserRepo
                .findById(applicationUserId)
                .orElseThrow(() -> new IllegalStateException("User not found in the database"));
        customer.setUser(applicationUser);

        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.getId();
    }

    protected void registerUser(CreateUserRequest request) {
        String registerUrl = "http://localhost:" + port + "/api/v1/auth/register";
        HttpEntity<String> registerRequest = new HttpEntity<>(String.format("{\"email\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"password\":\"%s\",\"role\":\"%s\",\"age\":\"%s\",\"address\":\"%s\",\"phoneNumber\":\"%s\"}",
                request.getEmail(), request.getFirstName(), request.getLastName(), request.getPassword(), request.getRole(), request.getAge(), request.getAddress(), request.getPhoneNumber()), headers);
        restTemplate.postForEntity(registerUrl, registerRequest, String.class);
    }

    protected String getJwtToken(CreateUserRequest request) throws JsonProcessingException {
        String tokenUrl = "http://localhost:" + port + "/api/v1/auth/token";
        HttpEntity<String> tokenRequest = new HttpEntity<>(String.format("{\"email\":\"%s\",\"password\":\"%s\"}", request.getEmail(), request.getPassword()), headers);
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, String.class);

        System.out.println("Response body: " + tokenResponse.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(tokenResponse.getBody());
        System.out.println(jsonNode);
        return jsonNode.get("data").get("accessToken").asText();
    }
}