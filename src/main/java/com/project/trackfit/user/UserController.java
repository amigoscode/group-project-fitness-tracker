package com.project.trackfit.user;

import com.project.trackfit.core.APICustomResponse;
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
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<APICustomResponse> createUser(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        UUID userId = userService.createUser(userCreationRequest);
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
