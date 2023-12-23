package com.project.trackfit.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTests {

    @Mock
    private ResourceNotFoundException resourceNotFoundException;

    @Mock
    private MeasurementNotFoundException measurementNotFoundException;

    @Mock
    private EmailAlreadyTakenException emailAlreadyTakenException;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("Check that ResourceNotFoundException returns corresponding error response")
    public void handleResourceNotFoundException_shouldReturnErrorResponse() {
        when(resourceNotFoundException.getMessage()).thenReturn("User Doesn't Exist");
        ErrorResponse response = globalExceptionHandler.handleResourceNotFoundException(resourceNotFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
        assertEquals("User Doesn't Exist", response.getMessage());
        assertEquals("User Doesn't Exist", response.getEx().getMessage());
    }

    @Test
    @DisplayName("Check that MeasurementNotFoundException returns corresponding error response")
    public void handleMeasurementNotFoundException_shouldReturnErrorResponse() {
        when(measurementNotFoundException.getMessage()).thenReturn("Measurement Doesn't Exist");
        ErrorResponse response = globalExceptionHandler.handleMeasurementNotFoundException(measurementNotFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
        assertEquals("Measurement Doesn't Exist", response.getMessage());
        assertEquals("Measurement Doesn't Exist", response.getEx().getMessage());
    }

    @Test
    @DisplayName("Check that EmailAlreadyTakenException returns corresponding error response")
    public void handleEmailAlreadyTakenException_shouldReturnErrorResponse() {
        when(emailAlreadyTakenException.getMessage()).thenReturn("Email already taken");
        ErrorResponse response = globalExceptionHandler.handleEmailAlreadyTakenException(emailAlreadyTakenException);
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals("Email already taken", response.getMessage());
        assertEquals("Email already taken", response.getEx().getMessage());
    }
}
