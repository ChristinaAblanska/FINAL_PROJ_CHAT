package com.example.chat.dto;

import com.example.chat.validation.UniqueUserName;
import com.example.chat.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        @NotNull
        @Email
        String email,
        @NotNull
        @UniqueUserName
        String userName,
        @NotNull
        @ValidPassword
        String password
) {
    public UserRequest withEncodedPass(String encodedPass) {
        return new UserRequest(firstName, lastName, email, userName, encodedPass);
    }
}