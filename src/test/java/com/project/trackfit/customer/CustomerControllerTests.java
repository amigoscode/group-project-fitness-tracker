package com.project.trackfit.customer;

import com.project.trackfit.core.model.APICustomResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateCustomer() {
        Customer newCustomer = new Customer(
                UUID.randomUUID(),
                "Jane",
                "Doe",
                25,
                "janedoe@example.com",
                "456 Main St"
        );

        ResponseEntity<APICustomResponse> response = restTemplate.postForEntity(
                "/api/v1/customers",
                newCustomer,
                APICustomResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof Map);
        assertTrue(((Map<?, ?>) response.getBody().getData()).containsKey("customerId"));

        UUID customerId = UUID.fromString(((Map<?, ?>) response.getBody().getData()).get("customerId").toString());
        assertNotNull(customerId);

        Optional<Customer> savedCustomer = customerRepository.findById(customerId);
        assertTrue(savedCustomer.isPresent());
        assertEquals("Jane", savedCustomer.get().getFirstName());
        assertEquals("Doe", savedCustomer.get().getLastName());
        assertEquals(25, savedCustomer.get().getAge());
        assertEquals("janedoe@example.com", savedCustomer.get().getEmail());
        assertEquals("456 Main St", savedCustomer.get().getAddress());
    }
}