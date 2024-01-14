package com.project.trackfit.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return false;
        }

        String countryCode = phoneNumber.substring(0, 4);
        return phoneNumber.startsWith("00") && CountryCode.isValidCode(countryCode);
    }
}
