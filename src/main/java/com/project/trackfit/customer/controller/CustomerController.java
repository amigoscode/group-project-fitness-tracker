package com.project.trackfit.customer.controller;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.customer.dto.CustomerResponse;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.customer.service.ICustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
        CustomerResponse customerRequest = service.getCustomerById(customer_id);
        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Customer", customerRequest))
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
        CustomerResponse customerRequest = service.updateCustomer(customerID, updateCustomerRequest);
        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Customer", customerRequest))
                        .message("Customer has been updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    @PostMapping(
            value = "{customerId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<APICustomResponse> uploadImage(
            @PathVariable("customerId") UUID customerId,
            @RequestParam("image") MultipartFile image
    ) {
        service.uploadImageForCustomer(customerId, image);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Image uploaded successfully")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("{customerId}/{mediaId}")
    public ResponseEntity<APICustomResponse> getImage(
            @PathVariable("customerId") UUID customerId,
            @PathVariable("mediaId") UUID mediaId
    ) {
        byte[] imageData = service.getImage(customerId, mediaId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Image retrieved successfully")
                        .data(imageData)
                        .build()
        );
    }
}
