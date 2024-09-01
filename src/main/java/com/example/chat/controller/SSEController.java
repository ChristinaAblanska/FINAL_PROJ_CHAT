package com.example.chat.controller;

import com.example.chat.dto.ChatDTO;
import com.example.chat.service.EventHandlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class SSEController {
    private final EventHandlerService eventHandlerService;

    @Operation(summary = "Send message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })})

    @PostMapping(value = ("/message"), consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(Principal principal, @RequestBody ChatDTO chatDTO) {
            boolean sent = eventHandlerService.handleMessage(principal.getName(), chatDTO);
            if (!sent) {
                return new ResponseEntity<>("Message not sent!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Message sent!", HttpStatus.OK);
    }

}