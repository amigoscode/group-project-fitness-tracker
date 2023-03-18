package com.project.trackfit.core;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/register")
public class ApplicationUserController {

    private  final ApplicationUserService applicationUserService;

    @PostMapping
    public ResponseEntity<APICustomResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest)
            throws NoSuchAlgorithmException {
        UUID userId = applicationUserService.createUser(createUserRequest);
        Map<String, UUID> data = new HashMap<>();
        data.put("User_Id", userId);
        return new ResponseEntity(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Personal Trainer have been Created Successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build(),
                CREATED
        );
    }
}
