package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Post {
    Integer postID;
    String title;
    String bodytext;
    Timestamp creationDate;
    Integer rating;
    Integer userID;
    Integer subforumID;
    Timestamp lastupdated;

    public Post(Integer postID, String title, String bodytext, Timestamp creationDate, Integer rating, Integer userID, Integer subforumID, Timestamp lastupdated) {
        this.postID = postID;
        this.title = title;
        this.bodytext = bodytext;
        this.creationDate = creationDate;
        this.rating = rating;
        this.userID = userID;
        this.subforumID = subforumID;
        this.lastupdated = lastupdated;
    }
    public Integer getPostID() {
        return postID;
    }
    public String getTitle() {
        return title;
    }
    public String getBodyText() {
        return bodytext;
    }
    public Timestamp getCreationDate() {
        return creationDate;
    }
    public Integer getRating() {
        return rating;
    }
    public Integer getUserID() {
        return userID;
    }
    public Integer getSubforumID() {
        return subforumID;
    }
    public Timestamp getLastUpdated() {
        return lastupdated;
    }
}