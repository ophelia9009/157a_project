package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Comment {
    private Long CommentID;
    private String CommentText;
    private Timestamp CreationDate = new Timestamp(System.currentTimeMillis());
    private Long Rating = 0L;
    private Long UserID;
    private Long PostID;
    private Timestamp LastUpdated = new Timestamp(System.currentTimeMillis());

    public Long getCommentID() {
        return CommentID;
    }

    public String getCommentText() {
        return CommentText;
    }

    public Timestamp getCreationDate() {
        return CreationDate;
    }

    public Long getUserID() {
        return UserID;
    }

    public Long getRating() {
        return Rating;
    }

    public Long getPostID() {
        return PostID;
    }


    public Timestamp getLastUpdated() {
        return LastUpdated;
    }

    public Comment(Long commentID, String commentText, Timestamp creationDate, Long rating, Long userID, Long postID, Timestamp lastUpdated) {
        CommentID = commentID;
        CommentText = commentText;
        CreationDate = creationDate != null ? creationDate : new Timestamp(System.currentTimeMillis());
        Rating = rating != null ? rating : 0;
        UserID = userID;
        PostID = postID;
        LastUpdated = lastUpdated != null ? lastUpdated : new Timestamp(System.currentTimeMillis());
    }
}
