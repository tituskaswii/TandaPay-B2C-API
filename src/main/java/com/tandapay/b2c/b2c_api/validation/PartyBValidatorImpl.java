package com.tandapay.b2c.b2c_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PartyBValidatorImpl implements ConstraintValidator<PartyBValidator, String> {

    @Override
    public void initialize(PartyBValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Custom validation logic for PartyB
        return value != null && value.matches("^[0-9]{12}$");
    }
}