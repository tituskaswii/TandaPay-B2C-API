package com.tandapay.b2c.b2c_api.validation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;

@Documented
@Constraint(validatedBy = PartyBValidatorImpl.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PartyBValidator {
    String message() default "Invalid PartyB number";
    Class<?>[] groups() default {};
   // Class<? extends Payload>[] payload() default {};
}