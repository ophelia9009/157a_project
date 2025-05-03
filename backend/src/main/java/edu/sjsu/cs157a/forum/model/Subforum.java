package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Subforum {
    private Integer subforumID;
    private String name;
    private Timestamp creationDate;
    private String description;
    private String subscriberCount;
    private Timestamp lastUpdated;
    private Integer ownerID;

    public Subforum (Integer subforumID, String name, Timestamp creationDate, String description, String subscriberCount, Timestamp lastUpdated, Integer ownerID) {
        this.subforumID = subforumID;
        this.name = name;
        this.creationDate = creationDate;
        this.description = description;
        this.subscriberCount = subscriberCount;
        this.lastUpdated = lastUpdated;
        this.ownerID = ownerID;
    }

    public Integer getSubforumID() {
        return subforumID;
    }
    public void setSubforumID(Integer subforumID) {
        this.subforumID = subforumID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Timestamp getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSubscriberCount() {
        return subscriberCount;
    }
    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public Integer getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }
}
