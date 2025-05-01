package com.example.servelet;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserProfileServletTest {
    private UserProfileServlet servlet;
    private UserDAO userDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws IOException {
        servlet = new UserProfileServlet();
        userDAO = mock(UserDAO.class);
        servlet.userDAO = userDAO;
        
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoPostRegister_Success() throws Exception {
        // Setup request
        when(request.getPathInfo()).thenReturn("/register");
        
        // Create test user data
        User testUser = new User(
            "123", 
            "testuser",
            "testpass",
            "test@example.com",
            new Timestamp(System.currentTimeMillis())
        );
        
        String json = new Gson().toJson(testUser);
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(reader);
        
        // Mock DAO response
        when(userDAO.createUser(any(User.class))).thenReturn(testUser);
        
        // Execute
        servlet.doPost(request, response);
        
        // Verify
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        
        writer.flush();
        String responseJson = stringWriter.toString();
        User responseUser = new Gson().fromJson(responseJson, User.class);
        assertEquals(testUser.getUserID(), responseUser.getUserID());
        assertEquals(testUser.getUsername(), responseUser.getUsername());
        assertEquals(testUser.getEmail(), responseUser.getEmail());
    }

    @Test
    public void testDoPostRegister_InvalidPath() throws Exception {
        when(request.getPathInfo()).thenReturn("/invalid");
        
        servlet.doPost(request, response);
        
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoPostRegister_InvalidJson() throws Exception {
        when(request.getPathInfo()).thenReturn("/register");
        
        BufferedReader reader = new BufferedReader(new StringReader("invalid json"));
        when(request.getReader()).thenReturn(reader);
        
        servlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writer.flush();
        assertTrue(stringWriter.toString().contains("Registration failed"));
    }
    @Test
    public void testDoPostLogin_Success() throws Exception {
        // Setup request
        when(request.getPathInfo()).thenReturn("/login");
        
        // Create test user data
        User testUser = new User(
            "123",
            "testuser",
            "testpass",
            "test@example.com",
            new Timestamp(System.currentTimeMillis())
        );
        
        String json = new Gson().toJson(testUser);
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(reader);
        
        // Mock DAO responses
        when(userDAO.getPasswordByUsername(testUser.getUsername())).thenReturn(testUser.getPassword());
        when(userDAO.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        
        // Execute
        servlet.doPost(request, response);
        
        // Verify
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        
        writer.flush();
        String responseJson = stringWriter.toString();
        User responseUser = new Gson().fromJson(responseJson, User.class);
        assertEquals(testUser.getUserID(), responseUser.getUserID());
        assertEquals(testUser.getUsername(), responseUser.getUsername());
        assertEquals(testUser.getEmail(), responseUser.getEmail());
    }

    @Test
    public void testDoPostLogin_InvalidCredentials() throws Exception {
        // Setup request
        when(request.getPathInfo()).thenReturn("/login");
        
        // Create test user data
        User testUser = new User(
            "123",
            "testuser",
            "wrongpass", // incorrect password
            "test@example.com",
            new Timestamp(System.currentTimeMillis())
        );
        
        String json = new Gson().toJson(testUser);
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(reader);
        
        // Mock DAO response
        when(userDAO.getPasswordByUsername(testUser.getUsername())).thenReturn("correctpass");
        
        // Execute
        servlet.doPost(request, response);
        
        // Verify
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writer.flush();
        assertTrue(stringWriter.toString().contains("Invalid credentials"));
    }
}