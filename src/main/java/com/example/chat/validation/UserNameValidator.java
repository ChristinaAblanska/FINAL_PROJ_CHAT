//package com.example.chat.validation;
//
//import com.example.chat.model.User;
//import com.example.chat.repository.UserRepository;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class UserNameValidator implements ConstraintValidator<UniqueUserName, String> {
//    private final UserRepository userRepository;
//    @Override
//    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
//        User user = userRepository.getByUserName(s);
//        return user == null;
//    }
//}