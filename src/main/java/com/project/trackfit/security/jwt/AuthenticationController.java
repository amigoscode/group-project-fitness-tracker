package com.project.trackfit.security.jwt;

import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.trainer.PersonalTrainer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import static com.project.trackfit.core.Role.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/auth/token")
class AuthenticateController {

    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AuthenticationResponse authenticateCustomer(
            @RequestBody AuthenticationRequest req
    ) throws Exception {
      ApplicationUser user;

        try {
            user = applicationConfig.authenticate(req.email(), req.password());
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        var userDetails = applicationConfig.loadUserByUsername(user.getEmail());

        var jwt = jwtService.generateToken(user.getEmail(), CUSTOMER);

        return new AuthenticationResponse(jwt);
    }

}