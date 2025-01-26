package com.example.userapi;

import com.example.userapi.controller.UserController;
import com.example.userapi.model.User;
import com.example.userapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.io.IOException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserapiApplication.class)
public class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "Doe", "Smith", 30, "Male", "john.doe@example.com",
                "1234567890", "johndoe", "password", "1994-01-01", "image.jpg",
                "O+", 180.0, 75.0, "Blue", null, null, null, null, null, null, null, null);
    }

    @Test
    void testImportUsersSuccess() throws IOException{
        doNothing().when(userService).saveUsersFromJson();
        String response = userController.importUsers();
        assertEquals("Users imported successfully!", response);
        verify(userService, times(1)).saveUsersFromJson();
    }

    @Test
    void testImportUsersFailure() throws IOException {
        doThrow(new RuntimeException("Import failed")).when(userService).saveUsersFromJson();
        String response = userController.importUsers();
        assertTrue(response.contains("Failed to import users"));
        verify(userService, times(1)).saveUsersFromJson();
    }

    @Test
    void testSearchUsers() {
        List<User> users = Arrays.asList(user);
        when(userService.searchUsers("John")).thenReturn(users);
        List<User> result = userController.searchUsers("John");
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(userService, times(1)).searchUsers("John");
    }

    @Test
    void testFindUserById() {
        when(userService.findUserByIdOrEmail(1L, null)).thenReturn(user);
        User result = userController.findUser(1L, null);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userService, times(1)).findUserByIdOrEmail(1L, null);
    }

    @Test
    void testFindUserByEmail() {
        when(userService.findUserByIdOrEmail(null, "john.doe@example.com")).thenReturn(user);
        User result = userController.findUser(null, "john.doe@example.com");
        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userService, times(1)).findUserByIdOrEmail(null, "john.doe@example.com");
    }
    @Test
    void testFindUserByIdNotFound() {
        when(userService.findUserByIdOrEmail(999L, null)).thenReturn(null);
        User result = userController.findUser(999L, null);
        assertNull(result);
        verify(userService, times(1)).findUserByIdOrEmail(999L, null);
    }

    @Test
    void testFindUserByEmailNotFound() {
        when(userService.findUserByIdOrEmail(null, "nonexistent@example.com")).thenReturn(null);
        User result = userController.findUser(null, "nonexistent@example.com");
        assertNull(result);
        verify(userService, times(1)).findUserByIdOrEmail(null, "nonexistent@example.com");
    }

    @Test
    void testSearchUsersNoResults() {
        List<User> emptyList = Arrays.asList();
        when(userService.searchUsers("NonExistentName")).thenReturn(emptyList);
        List<User> result = userController.searchUsers("NonExistentName");
        assertTrue(result.isEmpty());
        verify(userService, times(1)).searchUsers("NonExistentName");
    }

    @Test
    void testSearchUsersMultipleResults() {
        User user2 = new User(2L, "Jane", "Doe", "Smith", 28, "Female", "jane.doe@example.com",
                "0987654321", "janedoe", "password", "1995-05-05", "image2.jpg",
                "A+", 170.0, 60.0, "Green", null, null, null, null, null, null, null, null);
        List<User> users = Arrays.asList(user, user2);
        when(userService.searchUsers("Doe")).thenReturn(users);
        List<User> result = userController.searchUsers("Doe");
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(userService, times(1)).searchUsers("Doe");
    }
}
