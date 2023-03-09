package com.project.trackfit.customer;

import com.project.trackfit.core.model.APICustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Spring Boot REST API creates a User
     * http://[::1]:8080/api/v1/customers/
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> createUser(@Valid @RequestBody Customer customer) {
        Customer savedUserId = customerService.createCustomer(customer);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("customerId", savedUserId.getId()))
                        .message("Customer have been created successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Spring Boot REST API gets a User by Id
     * http://[::1]:8080/api/v1/users/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getUserById(@PathVariable("id") UUID customer_id) {
        RetrieveCustomerRequest customerRequest = customerService.RetrieveCustomerById(customer_id);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("customer", customerRequest))
                        .message("Customer have been created successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}