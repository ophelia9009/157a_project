package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Post {
    Long postID;
    String title;
    String bodytext;
    Timestamp creationDate;
    Long rating;
    Long userID;
    Long subforumID;
    Timestamp lastupdated;

    public Post(Long postID, String title, String bodytext, Timestamp creationDate, Long rating, Long userID, Long subforumID, Timestamp lastupdated) {
        this.postID = postID;
        this.title = title;
        this.bodytext = bodytext;
        this.creationDate = creationDate;
        this.rating = rating;
        this.userID = userID;
        this.subforumID = subforumID;
        this.lastupdated = lastupdated;
    }
    public Long getPostID() {
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
    public Long getRating() {
        return rating;
    }
    public Long getUserID() {
        return userID;
    }
    public Long getSubforumID() {
        return subforumID;
    }
    public Timestamp getLastUpdated() {
        return lastupdated;
    }
}