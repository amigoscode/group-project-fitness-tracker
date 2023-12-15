package com.project.trackfit.customer;

import com.project.trackfit.core.APICustomResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final ICustomerService service;

    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getCustomerById(
            @PathVariable("id") UUID customer_id) {
        RetrieveCustomerRequest customerRequest = service.RetrieveCustomerById(customer_id);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("customer", customerRequest))
                        .message("Customer has been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    @PutMapping("{id}")
    public  ResponseEntity<APICustomResponse>updateCustomerById(
            @PathVariable("id") UUID customerID,
            @RequestBody UpdateCustomerRequest updateCustomerRequest){
        RetrieveCustomerRequest customerRequest = service.updateCustomer(customerID, updateCustomerRequest);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("customer", customerRequest))
                        .message("Customer has been updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }
}
