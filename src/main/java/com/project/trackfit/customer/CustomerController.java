package com.project.trackfit.customer;

import com.project.trackfit.core.model.CustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Spring Boot REST API creates a User
     * http://[::1]:8080/api/v1/users/
     */
    @PostMapping
    public ResponseEntity<CustomResponse> createUser(@Valid @RequestBody Customer customer){
        Customer savedUserId = customerService.createCustomer(customer);
        return  ResponseEntity.ok(
                CustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("customer_id",savedUserId.getCustomer_id()))
                        .message("Customer have been Created Successfully")
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
    public ResponseEntity<Customer> getUserById(@PathVariable("id") Long userId) {
        Customer customer = customerService.getCustomerById(userId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}