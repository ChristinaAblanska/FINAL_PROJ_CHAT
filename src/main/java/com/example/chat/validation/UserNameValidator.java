package com.example.chat.validation;

import com.example.chat.model.User;
import com.example.chat.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserNameValidator implements ConstraintValidator<UniqueUserName, String> {
    private final UserService useruserService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        User user =  useruserService.getUserByUserName(s);
        return user == null;
    }

}