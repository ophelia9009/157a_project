package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class User {
    private Integer userID;
    private String username;
    private String password;
    private String email;
    private Timestamp registerDate;

    public User(Integer userID, String username, String password, String email, Timestamp registerDate) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
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