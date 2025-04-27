package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {


    public Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost:3306/sf_db", "root",
                    "password");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Something is wrong", e);
        }

    }

}
