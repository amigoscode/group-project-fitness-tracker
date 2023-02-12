package com.project.trackfit.control;

import com.project.trackfit.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customers")
public class MainController {

    /**
     * Spring Boot REST API returns a Customer
     * http://[::1]:8080/customers/customer
     */
    @GetMapping("customer")
    public ResponseEntity<Customer> getCustomer(){
        Customer customer = new Customer(
                1,
                "Andreas",
                "Kreouzos",
                37,
                "andreas.kreouzos@email.com",
                "Athens, Greece"
                );
        return ResponseEntity.ok().body(customer);
    }
}