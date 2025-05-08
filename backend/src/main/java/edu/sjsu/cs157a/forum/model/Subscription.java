package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Subscription {
    private Long userID;
    private Long subforumID;
    private Timestamp subscriptionDate;
    
    public Subscription(Long userID, Long subforumID, Timestamp subscriptionDate) {
        this.userID = userID;
        this.subforumID = subforumID;
        this.subscriptionDate = subscriptionDate;
    }
    
    // Constructor for creating a new subscription (without timestamp)
    public Subscription(Long userID, Long subforumID) {
        this(userID, subforumID, new Timestamp(System.currentTimeMillis()));
    }
    
    // Getters and setters
    public Long getUserID() {
        return userID;
    }
    
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    
    public Long getSubforumID() {
        return subforumID;
    }
    
    public void setSubforumID(Long subforumID) {
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