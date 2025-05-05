package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Subscription {
    private Integer userID;
    private Integer subforumID;
    private Timestamp subscriptionDate;
    
    public Subscription(Integer userID, Integer subforumID, Timestamp subscriptionDate) {
        this.userID = userID;
        this.subforumID = subforumID;
        this.subscriptionDate = subscriptionDate;
    }
    
    // Constructor for creating a new subscription (without timestamp)
    public Subscription(Integer userID, Integer subforumID) {
        this(userID, subforumID, new Timestamp(System.currentTimeMillis()));
    }
    
    // Getters and setters
    public Integer getUserID() {
        return userID;
    }
    
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    
    public Integer getSubforumID() {
        return subforumID;
    }
    
    public void setSubforumID(Integer subforumID) {
        this.subforumID = subforumID;
    }
    
    public Timestamp getSubscriptionDate() {
        return subscriptionDate;
    }
    
    public void setSubscriptionDate(Timestamp subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "userID=" + userID +
                ", subforumID=" + subforumID +
                ", subscriptionDate=" + subscriptionDate +
                '}';
    }
}