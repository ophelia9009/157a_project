package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Comment;
import edu.sjsu.cs157a.forum.model.Element;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class CommentDAO extends BaseDAO{

    private static final Logger logger = LogManager.getLogger(CommentDAO.class);



    public Comment createComment(String text, Long userId, Long postId){
        if (text == null)
            throw new IllegalArgumentException("text cannot be null");
        if (userId == null)
            throw new IllegalArgumentException("userID cannot be null");
        if (postId == null)
            throw new IllegalArgumentException("postId cannot be null");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO Comments (CommentText, CreationDate, UserID, PostID, LastUpdated) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, text);
            stmt.setTimestamp(2, now);
            stmt.setLong(3, userId);
            stmt.setLong(4, postId);
            stmt.setTimestamp(5, now);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating comment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long commentID = generatedKeys.getLong(1);
                    return new Comment(commentID, text, now, 0L, userId, postId, now);
                } else {
                    throw new SQLException("Creating comment failed, no ID obtained.");
                }
            }
        } catch (SQLException se) {
            logger.error("SQL ErrorState: " + se.getSQLState());
            logger.error("SQL ErrorCode: " + se.getErrorCode());
            se.printStackTrace();
            logger.error("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to create comment"+ se.getMessage(), se);
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

    public Comment updateComment(Long commentId, Long userId, String newText) throws SQLException {
        // Validate parameters before any database operations
        if (commentId == null || userId == null || newText == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String sql = "UPDATE Comments SET CommentText = ?, LastUpdated = CURRENT_TIMESTAMP() " +
                    "WHERE CommentID = ? AND UserID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newText);
            stmt.setLong(2, commentId);
            stmt.setLong(3, userId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating comment failed - comment not found or user not authorized");
            }

            // Get the original comment to preserve other fields
            String selectSql = "SELECT CommentText, CreationDate, Rating, PostID FROM Comments WHERE CommentID = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setLong(1, commentId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        String originalText = rs.getString("CommentText");
                        Timestamp creationDate = rs.getTimestamp("CreationDate");
                        Long rating = rs.getLong("Rating");
                        Long postId = rs.getLong("PostID");
                        return new Comment(commentId, newText, creationDate, rating, userId, postId, now);
                    }
                }
            }
            throw new SQLException("Failed to retrieve updated comment");
        }
    }

    public List<Comment> getCommentsByPost(Long postId) throws SQLException {
        if (postId == null) {
            throw new IllegalArgumentException("postId cannot be null");
        }

        String sql = "SELECT CommentID, CommentText, CreationDate, Rating, UserID, PostID, LastUpdated " +
                    "FROM Comments WHERE PostID = ? ORDER BY CreationDate DESC";

        List<Comment> comments = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(
                        rs.getLong("CommentID"),
                        rs.getString("CommentText"),
                        rs.getTimestamp("CreationDate"),
                        rs.getLong("Rating"),
                        rs.getLong("UserID"),
                        rs.getLong("PostID"),
                        rs.getTimestamp("LastUpdated")
                    ));
                }
            }
        }
        return comments;
    }

    public boolean deleteComment(Long commentId, Long userId) throws SQLException {
        if (commentId == null || userId == null) {
            throw new IllegalArgumentException("commentId and userId cannot be null");
        }

        String sql = "DELETE FROM Comments WHERE CommentID = ? AND UserID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, commentId);
            stmt.setLong(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
