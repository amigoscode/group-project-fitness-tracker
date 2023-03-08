package com.project.trackfit.core.exception;

import com.project.trackfit.core.model.APICustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<APICustomResponse> handleNotFoundException(Exception ex, WebRequest request) {
        ResponseEntity<APICustomResponse> NotFoundError = new ResponseEntity<>(

                APICustomResponse.builder()
                        .message(ex.getMessage())
                        .status(BAD_REQUEST).timeStamp(now())
                        .statusCode(BAD_REQUEST.value())
                        .developerMessage("Object was not found")
                        .build(), BAD_REQUEST);
        return NotFoundError;

    }
}
