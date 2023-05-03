package com.project.trackfit.customer;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.core.GenericController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/customers")
public class CustomerController extends GenericController {

    private final ICustomerService service;

    /**
     * Gets a Customer by Id
     * http://[::1]:8080/api/v1/customers/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getCustomerById(
            @PathVariable("id") UUID customer_id) {
        RetrieveCustomerRequest customerRequest = service.RetrieveCustomerById(customer_id);
        return createResponse(
                Map.of("customer", customerRequest),
                "Customer has been fetched successfully",
                OK);
    }
}
