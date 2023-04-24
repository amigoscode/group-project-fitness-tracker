package com.project.trackfit.customer;

import com.project.trackfit.core.APICustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/customers")
public class CustomerController {


    private final ICustomerService ICustomerService;

    /**
     * Spring Boot REST API gets a User by Id
     * http://[::1]:8080/api/v1/users/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getCustomerById(@PathVariable("id") UUID customer_id) {
        RetrieveCustomerRequest customerRequest = ICustomerService.RetrieveCustomerById(customer_id);
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
    @PatchMapping("{id}")
    public ResponseEntity<APICustomResponse>updateUser(
            @PathVariable("id")UUID customerId,
            @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest)
            throws NoSuchAlgorithmException{
        RetrieveCustomerRequest customerRequest=ICustomerService.updateCustomer(customerId,updateCustomerRequest);


        return new ResponseEntity(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("customer", customerRequest))
                        .message("Customer have been updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build(),
                OK
        );
    }
}