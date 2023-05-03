package com.project.trackfit.core;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/register")
public class ApplicationUserController extends GenericController {

    private final IApplicationUserService IApplicationUserService;

    /**
     * Creates a User
     * http://[::1]:8080/api/v1/auth/register
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        UUID userId = IApplicationUserService.createUser(createUserRequest);
        return createResponse(
                Map.of("User_Id", userId),
                "Application user has been created successfully",
                CREATED);
    }
}
