package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.utils.JWTUtil;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    private AuthController authController;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        authController = new AuthController(userDAO);
    }

    @Test
    void testLoginSuccess() {
        // Mock user with correct hashed password
        String hashedPassword = BCrypt.hashpw("testpassword", BCrypt.gensalt());
        User mockUser = new User(1, "testuser", hashedPassword, "USER", "", "", "", "");

        when(userDAO.getUserByUsername("testuser")).thenReturn(mockUser);
        when(userDAO.validateUser("testuser", "testpassword")).thenReturn(true);
        when(userDAO.getUserRole("testuser")).thenReturn("USER");

        Response response = authController.login(new User(0, "testuser", "testpassword", "USER", "", "", "", ""));
        assertEquals(200, response.getStatus());
    }

    @Test
    void testLoginFailure() {
        when(userDAO.validateUser("invaliduser", "wrongpassword")).thenReturn(false);

        Response response = authController.login(new User(0, "invaliduser", "wrongpassword", "USER", "", "", "", ""));
        assertEquals(401, response.getStatus());
    }
}
