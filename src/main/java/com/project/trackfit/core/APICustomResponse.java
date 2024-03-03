package com.project.trackfit.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class APICustomResponse {
    protected LocalDateTime timeStamp;
    protected int statusCode;
    @JsonIgnore
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Object data;

    public APICustomResponse() {
    }

    public APICustomResponse(LocalDateTime timeStamp,
                             int statusCode,
                             HttpStatus status,
                             String reason,
                             String message,
                             String developerMessage,
                             Object data) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.developerMessage = developerMessage;
        this.data = data;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class APICustomResponseBuilder {
        private LocalDateTime timeStamp;
        private int statusCode;
        private HttpStatus status;
        private String reason;
        private String message;
        private String developerMessage;
        private Object data;

        public APICustomResponseBuilder timeStamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public APICustomResponseBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public APICustomResponseBuilder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public APICustomResponseBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public APICustomResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public APICustomResponseBuilder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public APICustomResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public APICustomResponse build() {
            return new APICustomResponse(
                    timeStamp,
                    statusCode,
                    status,
                    reason,
                    message,
                    developerMessage,
                    data);
        }
    }

    public static APICustomResponseBuilder builder() {
        return new APICustomResponseBuilder();
    }
}