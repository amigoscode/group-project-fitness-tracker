package com.project.trackfit.core.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private String message;
    private HttpStatus httpStatus;
    private String details;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}