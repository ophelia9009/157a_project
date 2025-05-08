package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class User {
    private Long userID;
    private String username;
    private String password;
    private String email;
    private Timestamp registerDate;

    public User(Long userID, String username, String password, String email, Timestamp registerDate) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
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