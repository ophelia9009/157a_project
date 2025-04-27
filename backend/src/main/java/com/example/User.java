package com.example;

import java.sql.Timestamp;

public class User {
    private String userID;
    private String username;
    private String password;
    private String email;
    private Timestamp registerDate;

    public User(String userID, String username, String password, String email, Timestamp registerDate) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getRegisterDate() {
        return registerDate;
    }
}