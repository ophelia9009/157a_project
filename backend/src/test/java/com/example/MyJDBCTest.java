package com.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyJDBCTest {

    @Test
    public void test_getConnection() {

        MyJDBC myJDBC = new MyJDBC();
        assertNotNull(myJDBC.getConnection());
    }
}
