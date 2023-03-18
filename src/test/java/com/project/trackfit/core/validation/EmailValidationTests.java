package com.project.trackfit.core.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = EmailValidator.class)
public class EmailValidationTests {

    @Autowired
    private EmailValidator validator;

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "test.test@example.com", "test+test@example.com"})
    @DisplayName("Check valid email addresses based on the RFC822 pattern")
    public void testValidEMailAddressUsingRFC822Regex(String emailAddress) {
        assertTrue(validator.checkMailPattern(emailAddress));
    }

    @ParameterizedTest
    @ValueSource(strings = {"testexample.com", "test.test.@example.com", ".test+test@example.com"})
    @DisplayName("Check invalid email addresses based on the RFC822 pattern")
    public void testInvalidEMailAddressUsingRFC822Regex(String emailAddress) {
        assertFalse(validator.checkMailPattern(emailAddress));
    }
}