package com.project.trackfit.customer;

import com.project.trackfit.core.model.APICustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<APICustomResponse> createCustomer(@Valid @RequestBody Customer customer) {
        Customer savedUserId = customerService.createCustomer(customer);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("customerId",savedUserId.getId()))
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
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") UUID userId) {
        Customer customer = customerService.getCustomerById(userId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}