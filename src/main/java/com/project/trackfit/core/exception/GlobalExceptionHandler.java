package com.project.trackfit.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("User Doesn't Exist");
        errorResponse.setEx(exception);
        errorResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        return errorResponse;
    }

    @ExceptionHandler(MeasurementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleMeasurementNotFoundException(MeasurementNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Measurement Doesn't Exist");
        errorResponse.setEx(exception);
        errorResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        return errorResponse;
    }

    @ExceptionHandler(EmailNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleEmailNotValidException(EmailNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Email not valid");
        errorResponse.setEx(exception);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return errorResponse;
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleEmailAlreadyTakenException(EmailAlreadyTakenException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Email already taken");
        errorResponse.setEx(exception);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return errorResponse;
    }
}