package com.project.trackfit.user.controller;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.user.dto.CreateUserRequest;
import com.project.trackfit.user.service.IApplicationUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/auth/register")
public class ApplicationUserController {

    private final IApplicationUserService userService;

    public ApplicationUserController(IApplicationUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<APICustomResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UUID userId = userService.createUser(createUserRequest);
        return ResponseEntity.status(CREATED)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("User_Id", userId))
                        .message("Application user has been created successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
                );
    }
}
