package com.project.trackfit.security.jwt;

import com.project.trackfit.customer.Customer;
import com.project.trackfit.trainer.PersonalTrainer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.project.trackfit.core.model.UserProfile.*;

@RestController
@AllArgsConstructor
class AuthenticateController {

    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;

    @RequestMapping(value = "api/v1/customers/token")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse authenticateCustomer(
            @RequestBody AuthenticationRequest req
    ) throws Exception {
        Customer customer;

        try {
            customer = applicationConfig.authenticateCustomer(req.email(), req.password());
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        var customerDetails = applicationConfig.customerDetailsService(customer.getEmail());

        var jwt = jwtService.generateToken(customer.getEmail(), CUSTOMER);

        return new AuthenticationResponse(jwt);
    }

    @RequestMapping(value = "api/v1/trainers/token")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse authenticateTrainer(
            @RequestBody AuthenticationRequest req
    ) throws Exception {
        PersonalTrainer personalTrainer;

        try {
            personalTrainer = applicationConfig.authenticateTrainer(req.email(), req.password());
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        var customerDetails = applicationConfig.trainerDetailsService(personalTrainer.getEmail());

        System.out.println(customerDetails);
        var jwt = jwtService.generateToken(personalTrainer.getEmail(), TRAINER);

        return new AuthenticationResponse(jwt);
    }
}