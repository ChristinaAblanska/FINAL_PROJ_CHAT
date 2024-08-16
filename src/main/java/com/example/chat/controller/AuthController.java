package com.example.chat.controller;

import com.example.chat.authentication.CustomAuthenticationProvider;
import com.example.chat.dto.UserRequest;
import com.example.chat.enumeration.UserStatus;
import com.example.chat.service.EventHandlerService;
import com.example.chat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final EventHandlerService eventHandlerService;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })})
    @PostMapping("/public/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpSession session) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        eventHandlerService.registerUser(username);
        return new ResponseEntity<>("redirect:/chat", HttpStatus.OK);
    }

    @Operation(summary = "User registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })})
    @PostMapping(value = "/public/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest userRequest) {
        String encodedPass = customAuthenticationProvider.getPasswordEncoder().encode(userRequest.password());
        userRequest = userRequest.withEncodedPass(encodedPass);
        logger.info("New user created, username: {}", userRequest.userName());
        userService.create(userRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "User logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful logout",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })})
    @PostMapping("/api/v1/logout")
    public ResponseEntity<String> logout(Principal principal) {
        SecurityContextHolder.clearContext();
        logger.info("User {} logged out", principal.getName());
        userService.updateStatus(principal.getName(), UserStatus.OFFLINE);

        return new ResponseEntity<>("redirect:/home", HttpStatus.OK);
    }
}