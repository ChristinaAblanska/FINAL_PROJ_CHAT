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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final EventHandlerService eventHandlerService;
    private final CustomAuthenticationProvider customAuthenticationProvider;

//    @Operation(summary = "User login")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully login",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = String.class)) })
    @PostMapping("/login")
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

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody UserRequest userRequest) {
        String encodedPass = customAuthenticationProvider.getPasswordEncoder().encode(userRequest.password());
        userRequest = userRequest.withEncodedPass(encodedPass);
        userService.create(userRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/logout")
    public ResponseEntity<String> logout(Principal principal) {
        SecurityContextHolder.clearContext();
        log.info("User {} logged out", principal.getName());
        userService.updateStatus(principal.getName(), UserStatus.OFFLINE);

        return new ResponseEntity<>("redirect:/home", HttpStatus.OK);
    }
}