package com.example.chat.service;

import com.example.chat.model.User;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = new User();
        user = getUser(username, user);
        return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    new ArrayList<>()
            );
    }

    private User getUser(String username, User user) {
        try {
            user = userRepository.getByUserName(username);
        } catch (Exception e) {
            if (e instanceof EmptyResultDataAccessException) {
                throw new UsernameNotFoundException("Incorrect username!");
            }
        }
        return user;
    }
}