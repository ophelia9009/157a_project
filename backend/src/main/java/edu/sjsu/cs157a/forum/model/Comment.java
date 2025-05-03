package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Comment {
    private Integer CommentID;
    private String CommentText;
    private Timestamp CreationDate;
    private Integer Rating = 0;
    private Integer UserID;
    private Integer PostID;
    private Integer ParentID;
    private Timestamp LastUpdated;

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

    public Integer getParentID() {
        return ParentID;
    }

    public Timestamp getLastUpdated() {
        return LastUpdated;
    }

    public Comment(Integer commentID, String commentText, Timestamp creationDate, Integer rating, Integer userID, Integer postID, Integer parentID, Timestamp lastUpdated) {
        CommentID = commentID;
        CommentText = commentText;
        CreationDate = creationDate;
        Rating = rating;
        UserID = userID;
        PostID = postID;
        ParentID = parentID;
        LastUpdated = lastUpdated;
    }
}
