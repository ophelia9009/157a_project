package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Comment {
    private Integer CommentID;
    private String CommentText;
    private Timestamp CreationDate = new Timestamp(System.currentTimeMillis());
    private Integer Rating = 0;
    private Integer UserID;
    private Integer PostID;
    private Timestamp LastUpdated = new Timestamp(System.currentTimeMillis());

    public Integer getCommentID() {
        return CommentID;
    }

    public String getCommentText() {
        return CommentText;
    }

    public Timestamp getCreationDate() {
        return CreationDate;
    }

    public Integer getUserID() {
        return UserID;
    }

    public Integer getRating() {
        return Rating;
    }

    public Integer getPostID() {
        return PostID;
    }


    public Timestamp getLastUpdated() {
        return LastUpdated;
    }

    public Comment(Integer commentID, String commentText, Timestamp creationDate, Integer rating, Integer userID, Integer postID, Timestamp lastUpdated) {
        CommentID = commentID;
        CommentText = commentText;
        CreationDate = creationDate != null ? creationDate : new Timestamp(System.currentTimeMillis());
        Rating = rating != null ? rating : 0;
        UserID = userID;
        PostID = postID;
        LastUpdated = lastUpdated != null ? lastUpdated : new Timestamp(System.currentTimeMillis());
    }
}
