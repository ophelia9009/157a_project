package com.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserDAOTest {

    @Test
    public void test_getConnection() {

        UserDAO userDAO = new UserDAO();
        assertNotNull(userDAO.getConnection());
    }

    @Test
    public void test_getPasswordByUsername() {

        UserDAO userDAO = new UserDAO();
        assertNull(userDAO.getPasswordByUsername("DoesntExist"));
        assertEquals( "flowery_pin", userDAO.getPasswordByUsername("flower_girl"));

    }

    @Test
    public void test_getAllUsers() {

        UserDAO userDAO = new UserDAO();
        assertTrue(userDAO.getAllUsers().size()>0);

    }

}
