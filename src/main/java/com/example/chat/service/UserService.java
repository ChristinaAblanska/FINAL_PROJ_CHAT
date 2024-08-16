package com.example.chat.service;

import com.example.chat.dto.UserRequest;
import com.example.chat.dto.UserResponse;
import com.example.chat.enumeration.UserStatus;
import com.example.chat.errorHandling.BusinessNotFound;
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
        User user = userRepository.getById(id);
        if (user == null) {
            BusinessNotFound businessNotFound = new BusinessNotFound("User with id: " + id + " not found!");
            logger.error("Error: getById: Userid: {} not found!", id, businessNotFound);
            throw businessNotFound;
        }
        return user;
    }

    public User getUserByUserName(String userName) {
        User user = userRepository.getByUserName(userName);
        if (user == null) {
            BusinessNotFound businessNotFound = new BusinessNotFound("User with userName: " + userName + " not found!");
            logger.error("Error: getById: userName: {} not found!", userName, businessNotFound);
            throw businessNotFound;
        }
        return user;
    }

    public UserResponse getUserResponseByUserName(String userName) {
        User user = userRepository.getByUserName(userName);
        if (user == null) {
            BusinessNotFound businessNotFound = new BusinessNotFound("User with userName: " + userName + " not found!");
            logger.error("Error: getUserResponseByUserName: userName: {} not found!", userName, businessNotFound);
            throw businessNotFound;
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

    public void delete(long id) {
        if (!existsById(id)) {
            BusinessNotFound businessNotFound = new BusinessNotFound("User with id: " + id + " not found!");
            logger.error("Error: deleteById: Userid: {} not found!", id, businessNotFound);
            throw businessNotFound;
        }
        userRepository.delete(id);
        logger.info("Request to DB: delete user with id: {}", id);
    }

    public boolean existsById(long id) {
        try {
            User user = getById(id);
            return user != null;
        } catch (BusinessNotFound e) {
            logger.error("Error: Userid: {} not found!", id, e);
            return false;
        }
    }

    public boolean existsByUserName(String userName) {
        try {
            UserResponse user = getUserResponseByUserName(userName);
            return user != null;
        } catch (BusinessNotFound e) {
            logger.error("Error: UserName: {} not found!", userName, e);
            return false;
        }
    }
}