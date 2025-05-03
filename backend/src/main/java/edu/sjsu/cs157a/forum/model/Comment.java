package edu.sjsu.cs157a.forum.model;

import java.sql.Timestamp;

public class Comment {
    private int CommentID;
    private String CommentText;
    private Timestamp CreationDate;
    private int Rating = 0;
    private int UserID;
    private int PostID;
    private int ParentID;
    private Timestamp LastUpdated;

    public int getCommentID() {
        return CommentID;
    }

    public String getCommentText() {
        return CommentText;
    }

    public Timestamp getCreationDate() {
        return CreationDate;
    }

    public int getUserID() {
        return UserID;
    }

    public int getRating() {
        return Rating;
    }

    public int getPostID() {
        return PostID;
    }

    public int getParentID() {
        return ParentID;
    }

    public Timestamp getLastUpdated() {
        return LastUpdated;
    }

    public Comment(int commentID, String commentText, Timestamp creationDate, int rating, int userID, int postID, int parentID, Timestamp lastUpdated) {
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
