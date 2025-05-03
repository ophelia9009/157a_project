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

    public Post(Integer postID, String title, String bodytext, Timestamp creationDate, Integer rating, Integer userID, Integer subforumID) {
        this.postID = postID;
        this.title = title;
        this.bodytext = bodytext;
        this.creationDate = creationDate;
        this.rating = rating;
        this.userID = userID;
        this.subforumID = subforumID;
    }
    public Integer getId() {
        return postID;
    }
    public String getTitle() {
        return title;
    }
    public String getBodytext() {
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
}