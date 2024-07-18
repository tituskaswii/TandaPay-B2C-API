// package com.tandapay.b2c.b2c_api.validation;

// import jakarta.validation.Constraint;
// import jakarta.validation.Payload;
// import java.lang.annotation.ElementType;
// import java.lang.annotation.Retention;
// import java.lang.annotation.RetentionPolicy;
// import java.lang.annotation.Target;

// @Constraint(validatedBy = PartyBValidatorImpl.class)
// @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE })
// @Retention(RetentionPolicy.RUNTIME)
// public @interface PartyBValidator {
//     String message() default "Invalid PartyB number";
//     Class<?>[] groups() default {};
//     Class<? extends Payload>[] payload() default {};
// }
