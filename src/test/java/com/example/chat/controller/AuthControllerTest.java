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
    void givenCorrectCredentials_whenLoginIn_ThenRedirectToChat() throws Exception {
                this.mockMvc.perform(post("/public/login")
                        .param("username", "Kate")
                        .param("password", "password")).andDo(print())
                .andExpect(status().isOk());

    }


    // Update userName and email with non-existing before running again!!!!
    @Test
    void givenCorrectDetails_whenCreatingANewUser_ThenReturnCreatedStatus() throws Exception {
        UserRequest userRequest = new UserRequest("Stella", "Artois",
                "stella.Artois@gmail.com", "StellA", "password");
        this.mockMvc.perform(post("/public/register")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser("Kate")
    void givenCorrectCredentials_whenLoginOut_ThenRedirectToHome() throws Exception {
        this.mockMvc.perform(post("/api/v1/logout")).andDo(print())
                .andExpect(status().isOk());
    }
}