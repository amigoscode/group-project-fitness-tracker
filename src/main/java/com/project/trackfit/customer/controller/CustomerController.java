package com.project.trackfit.customer.controller;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.customer.service.ICustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final ICustomerService service;

    public CustomerController(ICustomerService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getCustomerById(
            @PathVariable("id") UUID customer_id) {
        Customer customerRequest = service.getCustomerById(customer_id);
        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(mapCustomerData(customerRequest))
                        .message("Customer has been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    @PutMapping("{id}")
    public ResponseEntity<APICustomResponse> updateCustomerById(
            @PathVariable("id") UUID customerID,
            @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        Customer customerRequest = service.updateCustomer(customerID, updateCustomerRequest);
        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(mapCustomerData(customerRequest))
                        .message("Customer has been updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    private Map<String, Object> mapCustomerData(Customer customerRequest) {
        Map<String, Object> customerData = new HashMap<>();
        customerData.put("id", customerRequest.getId());
        customerData.put("firstName", customerRequest.getUser().getFirstName());
        customerData.put("lastName", customerRequest.getUser().getLastName());
        customerData.put("age", customerRequest.getUser().getAge());
        customerData.put("email", customerRequest.getUser().getEmail());
        customerData.put("address", customerRequest.getUser().getAddress());
        return customerData;
    }
}
