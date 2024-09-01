package com.example.chat.service;

import com.example.chat.dto.UserRequest;
import com.example.chat.dto.UserResponse;
import com.example.chat.enumeration.UserStatus;
import com.example.chat.model.User;
import com.example.chat.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserRepository userRepository;
    UserService userService;
    User user;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
        user = new User(28L, "Kate", "Winslett", "kate.winslett@gmail.com",
                "Kate",
                "$2a$10$u4eU9d.OFQTlJdYum1tykunFmy.rKdA565cwr/t0tfHAUf/of/jt2", UserStatus.ONLINE);
    }

    @Test
    void givenValidId_whenGettingUserById_thenReturnUser() {
        long id = user.getId();
        User expectedUser = user;
        Mockito.when(userRepository.getById(id)).thenReturn(expectedUser);
        User actualUser = userService.getById(id);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void givenInValidId_whenGettingUserById_thenReturnNull() {
        long id = user.getId();

        User userById = userService.getById(id);
        assertNull(userById);
    }

    @Test
    void givenInValidId_whenGettingUserById_thenExceptionThrown() {
        long id = user.getId();
        Mockito.when(userRepository.getById(id)).thenReturn(null);

        Mockito.when(userRepository.getById(id)).thenThrow(RuntimeException.class);
        assertThrows(Exception.class,
                () -> userService.getById(id));
    }

    @Test
    void givenValidUserName_whenGettingUserByUserName_thenReturnUser() {
        String userName = user.getUserName();
        User expectedUser = user;
        Mockito.when(userRepository.getByUserName(userName)).thenReturn(expectedUser);
        User actualUser = userService.getUserByUserName(userName);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void givenInValidUserName_whenGettingUserByUserName_thenReturnNULL() {
        String userName = user.getUserName();

        User userByUserName = userService.getUserByUserName(userName);
        assertNull(userByUserName);
    }

    @Test
    void givenInValidUserName_whenGettingUserByUserName_thenExceptionThrown() {
        String userName = user.getUserName();
        Mockito.when(userRepository.getByUserName(userName)).thenThrow(RuntimeException.class);
        assertThrows(Exception.class,
                () -> userService.getUserByUserName(userName));
    }

    @Test
    void givenValidUserName_whenGettingUserResponseByUserName_thenReturnUserResponse() {
        String userName = user.getUserName();
        UserResponse expectedUser = new UserResponse(user.getFirstName(), user.getLastName(),
                user.getUserName(), user.getEmail());
        Mockito.when(userRepository.getByUserName(userName)).thenReturn(user);
        UserResponse actualUser = userService.getUserResponseByUserName(userName);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void givenInValidUserName_whenGettingUserResponseByUserName_thenExceptionThrown() {
        String userName = "InvalidUserName";
        Mockito.when(userRepository.getByUserName(userName)).thenReturn(null);
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.getUserResponseByUserName(userName));
        String expectedMessage = "User with userName: " + userName + " not found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }



    @Test
    void givenValidUserName_whenGettingStatusByUserName_thenReturnStatus() {
        String userName = user.getUserName();
        UserStatus expectedStatus = user.getStatus();
        Mockito.when(userRepository.getStatusByUserName(userName)).thenReturn(expectedStatus);
        UserStatus actualStatus = userService.getStatus(userName);
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void create() {
        UserRequest userRequest = new UserRequest(user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getUserName(), user.getPassword());
        userService.create(userRequest);
        Mockito.verify(userRepository, Mockito.times(1)).save(userRequest.firstName(), userRequest.lastName(), userRequest.email(), userRequest.userName(),
                user.getPassword(), UserStatus.ONLINE.name());
    }

    @Test
    void updateStatusTest() {
        String userName = user.getUserName();
        userService.updateStatus(userName, UserStatus.OFFLINE);
        Mockito.verify(userRepository, Mockito.times(1))
                .updateStatusByUserName(userName, UserStatus.OFFLINE.name());
    }


    @Test
    void givenValidId_whenCheckingIfUserExistsById_thenReturnTRUE() {
        long id = user.getId();
        User expectedUser = user;
        Mockito.when(userRepository.getById(id)).thenReturn(expectedUser);
        assertTrue(userService.existsById(id));
    }

    @Test
    void givenInValidId_whenCheckingIfUserExistsById_thenReturnFALSE() {
        long id = user.getId();
        User expectedUser = null;
        Mockito.when(userRepository.getById(id)).thenReturn(expectedUser);
        assertFalse(userService.existsById(id));
    }

    @Test
    void givenValidUserName_whenCheckingIfUserExistsByUserName_thenReturnTRUE() {
        String userName = user.getUserName();
        User expectedUser = user;
        Mockito.when(userRepository.getByUserName(userName)).thenReturn(expectedUser);
        assertTrue(userService.existsByUserName(userName));
    }

    @Test
    void givenInValidUserName_whenCheckingIfUserExistsByUserName_thenReturnFALSE() {
        String userName = user.getUserName();
        User expectedUser = null;
        Mockito.when(userRepository.getByUserName(userName)).thenReturn(expectedUser);
        assertFalse(userService.existsByUserName(userName));
    }
}