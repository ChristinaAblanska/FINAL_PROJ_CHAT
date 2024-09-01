package com.example.chat.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = UserNameValidator.class
)
public @interface UniqueUserName {
    String message() default "UserName must be unique!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}