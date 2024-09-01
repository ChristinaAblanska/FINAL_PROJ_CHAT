package com.example.chat.service;

import com.example.chat.enumeration.MessageStatus;
import com.example.chat.model.Message;
import com.example.chat.model.User;
import com.example.chat.dto.ChatDTO;
import com.example.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void create(ChatDTO chatDTO, String fromUser, MessageStatus status, LocalDateTime timestamp) {
        User sender = userService.getUserByUserName(fromUser);
        User receiver = userService.getUserByUserName(chatDTO.userName());
        messageRepository.save(chatDTO.messageContent(), sender.getId(), receiver.getId(), status, timestamp);
    }

    public List<Message> getAllPendingTo(String userName) {
            User user = userService.getUserByUserName(userName);
            if (user == null) {
                logger.error("Error: userName: {} not found!", userName);
                return new ArrayList<>();
            }
            return messageRepository.getAllPendingByReceiverId(user.getId());
    }

    public void updateMessage(long messageId, MessageStatus status, LocalDateTime timestamp) {
        messageRepository.updateMessageById(messageId, status.toString(), timestamp);
    }
}