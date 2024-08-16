package com.example.chat.service;

import com.example.chat.controller.AuthController;
import com.example.chat.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public void sendSimpleEmail(EmailDTO emailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDTO.getToList());
        message.setSubject(emailDTO.getSubject());
        message.setText(emailDTO.getBody());

        logger.info("Request to EmailService: send email with subject " + emailDTO.getSubject()
                + " to email address: " + emailDTO.getToList());
        mailSender.send(message);
    }
}