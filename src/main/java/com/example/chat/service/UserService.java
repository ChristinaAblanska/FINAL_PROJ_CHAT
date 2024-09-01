package com.example.chat.service;

import com.example.chat.dto.UserRequest;
import com.example.chat.dto.UserResponse;
import com.example.chat.enumeration.UserStatus;
import com.example.chat.model.User;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getById(long id) {
        User user;
        try {
            user = userRepository.getById(id);
        } catch (Exception e) {
            if (e instanceof EmptyResultDataAccessException) {
                return null;
            }
            logger.error("Error: getById: Userid: {} not found!", id, e);
            throw e;
        }

        return user;
    }

    public User getUserByUserName(String userName) {
        User user;
        try {
            user = userRepository.getByUserName(userName);
        } catch (Exception e) {
            if (e instanceof EmptyResultDataAccessException) {
                return null;
            }
            logger.error("Error: getUserByUserName: userName: {} not found!", userName, e);
            throw e;
        }

        return user;
    }

    public UserResponse getUserResponseByUserName(String userName) {
        User user = userRepository.getByUserName(userName);
        if (user == null) {
            UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("User with userName: "
                    + userName + " not found!");
            logger.error("Error: getUserResponseByUserName: userName: {} not found!", userName, usernameNotFoundException);
            throw usernameNotFoundException;
        }

        return new UserResponse(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail());
    }

    public UserStatus getStatus(String userName) {
        return userRepository.getStatusByUserName(userName);
    }

    public void create(UserRequest userRequest) {
        logger.info("Request to DB: create new user with userName: {}", userRequest.userName());
        userRepository.save(userRequest.firstName(), userRequest.lastName(), userRequest.email(), userRequest.userName(),
                userRequest.password(), UserStatus.ONLINE.name());
    }

    public void updateStatus(String userName, UserStatus status) {
        userRepository.updateStatusByUserName(userName, status.name());
    }


    public boolean existsById(long id) {
            User user = getById(id);
            return user != null;
    }

    public boolean existsByUserName(String userName) {
            User user = getUserByUserName(userName);
            return user != null;
    }

}