package com.project.trackfit.exception;

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
    public ErrorResponse handleUserDoesNotExistException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("User Doesn't Exist");
        errorResponse.setEx(exception);
        return errorResponse;
    }

    @ExceptionHandler(EmailNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleEmailNotValidException(EmailNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Email not valid");
        errorResponse.setEx(exception);
        return errorResponse;
    }
}