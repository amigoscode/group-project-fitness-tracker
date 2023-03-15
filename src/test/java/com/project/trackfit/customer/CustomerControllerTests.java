package com.project.trackfit.customer;

import com.project.trackfit.core.model.APICustomResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer(
                UUID.randomUUID(),
                "John",
                "Doe",
                30,
                "john.doe@example.com",
                "123 Main St"
        );
        customerRepository.save(customer);
        UUID customerId = customer.getId();
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a customer and return 200 OK")
    void shouldCreateCustomerAndReturn200OK() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Jane",
                "Doe",
                25,
                "jane.doe@example.com",
                "456 High St"
        );
        ResponseEntity<APICustomResponse> responseEntity = restTemplate.postForEntity(
                "/api/v1/customers",
                request,
                APICustomResponse.class
        );

        assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getData());
        assertInstanceOf(Map.class, responseEntity.getBody().getData());
        Map<?, ?> data = (Map<?, ?>) responseEntity.getBody().getData();
        assertTrue(data.containsKey("Customer_ID"));
        assertNotNull(data.get("Customer_ID"));
    }

    @Test
    @DisplayName("Given existing CustomerId when GetCustomerById then returns customer details")
    void givenExistingCustomerId_whenGetCustomerById_thenReturnsCustomerDetails() {
        // Given
        Customer customer = customerRepository.save(new Customer(
                UUID.randomUUID(),
                "John",
                "Doe",
                25,
                "johndoe@gmail.com",
                "123 Main St"
        ));

        // When
        ResponseEntity<APICustomResponse> response =
                restTemplate.getForEntity("/api/v1/customers/" + customer.getId(), APICustomResponse.class);

        // Then
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody().getMessage(),"Fetched Customer");
        assertNotNull(response.getBody().getData());

        RetrieveCustomerRequest expected = new RetrieveCustomerRequest(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getAge(),
                customer.getEmail(),
                customer.getAddress()
        );

        assertEquals(expected.id(), customer.getId());
        assertEquals(expected.firstName(), customer.getFirstName());
        assertEquals(expected.lastName(), customer.getLastName());
        assertEquals(expected.age(), customer.getAge());
        assertEquals(expected.email(), customer.getEmail());
        assertEquals(expected.address(), customer.getAddress());
    }

    @Test
    @DisplayName("Given non existing CustomerId when GetCustomerById then returns not found")
    void givenNonExistingCustomerId_whenGetCustomerById_thenReturnsNotFound() {
        // When
        ResponseEntity<APICustomResponse> response =
                restTemplate.getForEntity("/api/v1/customers/" + UUID.randomUUID(), APICustomResponse.class);

        // Then
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody().getMessage(),"User Doesn't Exist");
        assertNull(response.getBody().getData());
    }
}