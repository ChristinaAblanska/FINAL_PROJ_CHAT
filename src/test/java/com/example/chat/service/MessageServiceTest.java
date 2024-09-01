package com.example.chat.service;

import com.example.chat.dto.ChatDTO;
import com.example.chat.enumeration.MessageStatus;
import com.example.chat.enumeration.UserStatus;
import com.example.chat.model.Message;
import com.example.chat.model.User;
import com.example.chat.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {
    MessageRepository messageRepository;
    UserService userService;
    MessageService messageService;
    User sender;
    User receiver;
    ChatDTO chatDTO;
    @BeforeEach
    void setUp() {
        messageRepository = Mockito.mock(MessageRepository.class);
        userService = Mockito.mock(UserService.class);
        messageService = new MessageService(messageRepository, userService);
        sender = new User(12L, "Dan", "Brown", "dan.brown@gmail.com",
                "danBrown",
                "$2a$10$W5fwnQAG.yqpBr4WJ7neFeUDmVxe4DOcCqIDd0O1QEPIuYCbImKIu", UserStatus.ONLINE);
        receiver = new User(8L,	"Kate","Winslett", "kate.winslett@gmail.com",
                "Kate",	"password",	UserStatus.ONLINE);
        chatDTO = new ChatDTO("Hello, user", receiver.getUserName());
    }

    @Test
    void create() {
        MessageStatus status = MessageStatus.SENT;
        String fromUser = sender.getUserName();
        LocalDateTime timestamp = LocalDateTime.now();
        Mockito.when(userService.getUserByUserName(fromUser)).thenReturn(sender);
        Mockito.when(userService.getUserByUserName(chatDTO.userName())).thenReturn(receiver);
        messageService.create(chatDTO, sender.getUserName(), status, timestamp);
        Mockito.verify(messageRepository, Mockito.times(1))
                .save(chatDTO.messageContent(), sender.getId(), receiver.getId(), status, timestamp);
    }

    @Test
    void givenValidUserName_whenGetAllPendingToUserName_thenReturnAListOfPendingMessages() {
        List<Message> expectedMessages = new ArrayList<>();
        LocalDateTime timestamp = LocalDateTime.now();
        for(int i = 0; i <= 4; i++) {
            Message message = new Message();
            message.setId(i);
            message.setContent("Hello, test: " + (i + 1));
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setStatus(MessageStatus.PENDING);
            message.setTimestamp(timestamp);
            expectedMessages.add(message);
        }

        String userName = receiver.getUserName();
        Mockito.when(userService.getUserByUserName(userName)).thenReturn(receiver);
        Mockito.when(messageRepository.getAllPendingByReceiverId(receiver.getId())).thenReturn(expectedMessages);
        List<Message> actualMessages = messageService.getAllPendingTo(receiver.getUserName());
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    void givenInValidUserName_whenGetAllPendingToUserName_thenReturnEmptyList() {
        List<Message> expectedMessages = new ArrayList<>();
        String userName = "InvalidUserName";

        Mockito.when(userService.getUserByUserName(userName)).thenReturn(null);
        Mockito.when(messageRepository.getAllPendingByReceiverId(receiver.getId())).thenReturn(expectedMessages);
        List<Message> actualMessages = messageService.getAllPendingTo(receiver.getUserName());
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    void updateMessage() {
        long id = 1L;
        LocalDateTime timestamp = LocalDateTime.now();
        MessageStatus status = MessageStatus.SENT;

        Message message = new Message();
        message.setId(id);
        message.setContent("Hello, test: " + id);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setStatus(MessageStatus.PENDING);
        message.setTimestamp(timestamp);

        messageService.updateMessage(id, status, timestamp);
        Mockito.verify(messageRepository, Mockito.times(1))
                .updateMessageById(id, status.toString(), timestamp);
    }
}