package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Comment;
import edu.sjsu.cs157a.forum.model.Element;

import java.sql.*;

public class CommentDAO extends BaseDAO{
    public Comment createComment(String text, Integer userId, Integer postId){
        if (text == null)
            throw new IllegalArgumentException("text cannot be null");
        if (userId == null)
            throw new IllegalArgumentException("userID cannot be null");
        if (postId == null)
            throw new IllegalArgumentException("postId cannot be null");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO comments (CommentText, CreationDate, UserID, PostID, LastUpdated) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, text);
            stmt.setTimestamp(2, now);
            stmt.setInt(3, userId);
            stmt.setInt(4, postId);
            stmt.setTimestamp(5, now);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating comment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer commentID = generatedKeys.getInt(1);
                    return new Comment(commentID, text, now, 0, userId, postId, now);
                } else {
                    throw new SQLException("Creating comment failed, no ID obtained.");
                }
            }
        } catch (SQLException se) {
            System.out.println("SQL ErrorState: " + se.getSQLState());
            System.out.println("SQL ErrorCode: " + se.getErrorCode());
            se.printStackTrace();
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to create comment", se);
        }
    }

    public Element createSelfReferencingComment(String text, int userId, int postId) throws SQLException {
        Element created = createTuple(new Element(
                "comments", "CommentID", null,
                new String[]{"CommentText", "UserID", "PostID", "ParentID"},
                new Object[]{text, userId, postId, 0} // placeholder
        ));

        Object id = created.getPrimaryKeyValue();
        updateTuple(new Element(
                "comments", "CommentID", id,
                new String[]{"ParentID"},
                new Object[]{id}
        ));

        return created;
    }

    public Comment updateComment(Integer commentId, Integer userId, String newText) throws SQLException {
        // Validate parameters before any database operations
        if (commentId == null || userId == null || newText == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String sql = "UPDATE comments SET CommentText = ?, LastUpdated = ? " +
                    "WHERE CommentID = ? AND UserID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newText);
            stmt.setTimestamp(2, now);
            stmt.setInt(3, commentId);
            stmt.setInt(4, userId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating comment failed - comment not found or user not authorized");
            }

            // Get the original comment to preserve other fields
            String selectSql = "SELECT CommentText, CreationDate, Rating, PostID FROM comments WHERE CommentID = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setInt(1, commentId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        String originalText = rs.getString("CommentText");
                        Timestamp creationDate = rs.getTimestamp("CreationDate");
                        Integer rating = rs.getInt("Rating");
                        Integer postId = rs.getInt("PostID");
                        return new Comment(commentId, newText, creationDate, rating, userId, postId, now);
                    }
                }
            }
            throw new SQLException("Failed to retrieve updated comment");
        }
    }
}
