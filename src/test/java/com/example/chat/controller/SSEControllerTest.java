package com.example.chat.controller;

import com.example.chat.dto.ChatDTO;
import com.example.chat.service.EventHandlerService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SSEControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventHandlerService eventHandlerService;

    @Test
    @WithMockUser("Kate")
    void givenValidOnlineReceiver_whenSendingMessage_thenMessageSentReturned() throws Exception {
        eventHandlerService.registerUser("johnSmith");
        ChatDTO chatDTO = new ChatDTO("Hello, John2222222!", "johnSmith");
        this.mockMvc.perform(post("/api/v1/message")
                                .content(objectMapper.writeValueAsString(chatDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("Kate")
    void givenValidOfflineReceiver_whenSendingMessage_thenMessageStoredAsPending() throws Exception {
        ChatDTO chatDTO = new ChatDTO("Hello, Simo!", "Simo");
        this.mockMvc.perform(post("/api/v1/message")
                        .content(objectMapper.writeValueAsString(chatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("Kate")
    void givenInValidReceiver_whenSendingMessage_thenMessageNotSent() throws Exception {
        ChatDTO chatDTO = new ChatDTO("Hello, Lisa!", "Lisa");
        this.mockMvc.perform(post("/api/v1/message")
                        .content(objectMapper.writeValueAsString(chatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }
}