package com.example.chat.dto;

import jakarta.validation.constraints.NotNull;

public record ChatDTO(
        @NotNull
        String messageContent,
        @NotNull
        String userName) {
}