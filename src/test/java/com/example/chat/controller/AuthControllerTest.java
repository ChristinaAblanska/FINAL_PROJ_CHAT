package com.example.chat.controller;

import com.example.chat.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenInValidUserName_whenLoginIn_ThenIsNotFound() throws Exception {
                this.mockMvc.perform(post("/public/login")
                        .param("username", "Kate1")
                        .param("password", "password")).andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void givenInValidPassword_whenLoginIn_ThenUnauthorized() throws Exception {
        this.mockMvc.perform(post("/public/login")
                        .param("username", "Kate")
                        .param("password", "password1")).andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    void givenCorrectCredentials_whenLoginIn_ThenRedirectToChat() throws Exception {
        this.mockMvc.perform(post("/public/login")
                        .param("username", "Kate")
                        .param("password", "password")).andDo(print())
                .andExpect(status().isOk());

    }


    // Update userName and email with non-existing before running again!!!!
    //jDGreen
    @Test
    void givenCorrectDetails_whenCreatingANewUser_ThenReturnCreatedStatus() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserRequest userRequest = new UserRequest("JasonD", "Green",
                uuid.toString() + "@gmail.com", uuid.toString(), "pasD@22Asword");
        this.mockMvc.perform(post("/public/register")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void givenInvalidDetails_whenCreatingANewUser_ThenBadRequest() throws Exception {
        UserRequest userRequest = new UserRequest("Jason", "Green",
                "kate", "Kate", "1234");
        this.mockMvc.perform(post("/public/register")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("Kate")
    void givenCorrectCredentials_whenLoginOut_ThenRedirectToHome() throws Exception {
        this.mockMvc.perform(post("/api/v1/logout")).andDo(print())
                .andExpect(status().isOk());
    }
}