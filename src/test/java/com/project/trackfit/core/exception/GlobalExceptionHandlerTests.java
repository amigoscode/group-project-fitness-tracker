package com.project.trackfit.core.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTests {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setup() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Check that ResourceNotFoundException returns corresponding error response")
    public void handleResourceNotFoundException_shouldReturnErrorResponse() {
        //given: an instance of ResourceNotFoundException
        ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException();

        //when: calling the method under test
        ErrorResponse response = globalExceptionHandler.handleResourceNotFoundException(resourceNotFoundException);

        //then: assert that the response is as expected
        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
        assertEquals("User Doesn't Exist", response.getMessage());
    }


    @Test
    @DisplayName("Check that MeasurementNotFoundException returns corresponding error response")
    public void handleMeasurementNotFoundException_shouldReturnErrorResponse() {
        //given: an instance of MeasurementNotFoundException
        MeasurementNotFoundException measurementNotFoundException = new MeasurementNotFoundException();

        //when: calling the method under test
        ErrorResponse response = globalExceptionHandler.handleMeasurementNotFoundException(measurementNotFoundException);

        //then: assert that the response is as expected
        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
        assertEquals("Measurement Doesn't Exist", response.getMessage());
    }


    @Test
    @DisplayName("Check that EmailAlreadyTakenException returns corresponding error response")
    public void handleEmailAlreadyTakenException_shouldReturnErrorResponse() {
        //given: an instance of EmailAlreadyTakenException with a specific email
        String testEmail = "test@example.com";
        EmailAlreadyTakenException emailAlreadyTakenException = new EmailAlreadyTakenException(testEmail);

        //when: calling the method under test
        ErrorResponse response = globalExceptionHandler.handleEmailAlreadyTakenException(emailAlreadyTakenException);

        //then: assert that the response is as expected
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals("Email already taken", response.getMessage());
        assertEquals("The email address '" + testEmail + "' is already in use. Please choose a different one.", response.getDetails());
    }
}
