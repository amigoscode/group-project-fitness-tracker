package com.project.trackfit.security.jwt;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.core.ApplicationUser;
import com.project.trackfit.core.GenericController;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.OK;

import java.util.Map;




@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/auth/token")
public class AuthenticationController  extends GenericController {

    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<APICustomResponse> authenticateCustomer(@RequestBody AuthenticationRequest req)
            throws Exception {
      ApplicationUser user;

        try {
            user = applicationConfig.authenticate(req.email(), req.password());
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        var userDetails = applicationConfig.loadUserByUsername(user.getEmail());

        var jwt = jwtService.generateToken(user.getEmail(), user.getRole());

        return createResponse(
                Map.of("accessToken",jwt,"userRole",user.getRole()),
                "User is authenticated successfully",
                OK
        ) ;
    }
}