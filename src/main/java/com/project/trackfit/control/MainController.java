package com.project.trackfit.control;

import com.project.trackfit.model.Customer;
import com.project.trackfit.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/customers")
public class MainController {

    private CustomerService customerService;

    /**
     * Spring Boot REST API returns a Customer
     * http://[::1]:8080/api/customers/customer
     */
    @PostMapping
    public ResponseEntity<Customer> createUser(@RequestBody Customer customer){
        Customer savedCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
}