package com.project.trackfit.exception;

public class UserDoesNotExistException extends Exception{
    public UserDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
