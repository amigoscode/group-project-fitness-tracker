package com.project.trackfit.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public abstract class GenericController {

    protected ResponseEntity<APICustomResponse> createResponse(Object data, String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(data)
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .build()
                );
    }
}
