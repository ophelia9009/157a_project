package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Comment;
import edu.sjsu.cs157a.forum.model.Element;

import java.sql.*;

public class CommentDAO extends BaseDAO{
    public Comment createComment(String text, Integer userId, Integer postId, Integer parentId){
        if (text == null)
            throw new IllegalArgumentException("text cannot be null");
        if (userId == null)
            throw new IllegalArgumentException("userID cannot be null");
        if (postId == null)
            throw new IllegalArgumentException("postId cannot be null");
        if (parentId == null)
            throw new IllegalArgumentException("parentId cannot be null");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO comments (CommentText, CreationDate, UserID, PostID, ParentID, LastUpdated) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, text);
            stmt.setTimestamp(2, now);
            stmt.setInt(3, userId);
            stmt.setInt(4, postId);
            stmt.setInt(5, parentId);
            stmt.setTimestamp(6, now);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating comment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer commentID = generatedKeys.getInt(1);
                    return new Comment(commentID, text, now, 0, userId, postId, parentId, now);
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

}
