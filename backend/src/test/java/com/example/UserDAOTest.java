package com.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserDAOTest {

    @Test
    public void test_getConnection() {

        UserDAO userDAO = new UserDAO();
        assertNotNull(userDAO.getConnection());
    }
}
