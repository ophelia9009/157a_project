package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Comment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CommentDAOTest {
    private CommentDAO commentDAO;
    private int testUserId;
    private int testPostId;

    @Before
    public void setUp() throws SQLException {
        commentDAO = new CommentDAO();
        
        // Create test user
        try (Connection conn = commentDAO.getConnection();
             Statement stmt = conn.createStatement()) {
            String randomSuffix = java.util.UUID.randomUUID().toString().substring(0, 8);
            String randomUsername = "testuser_" + randomSuffix;
            String randomEmail = "test+" + randomSuffix + "@test.com";
            stmt.executeUpdate("INSERT INTO Users (Username, Password, Email) VALUES ('" + randomUsername + "', 'password', '" + randomEmail + "')");
            try (ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                testUserId = rs.getInt(1);
            }

            // Create test subforum
            String randomSubforum = "testsub_" + java.util.UUID.randomUUID().toString().substring(0, 8);
            stmt.executeUpdate("INSERT INTO Subforums (Name, Description, OwnerID) VALUES ('" + randomSubforum + "', 'test desc', " + testUserId + ")");
            int subforumId;
            try (ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                subforumId = rs.getInt(1);
            }

            // Create test post
            stmt.executeUpdate("INSERT INTO Posts (Title, BodyText, UserID, SubforumID) VALUES ('test post', 'test content', " + testUserId + ", " + subforumId + ")");
            try (ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                testPostId = rs.getInt(1);
            }
        }
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up test data in reverse dependency order
        try (Connection conn = commentDAO.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM Comments WHERE UserID = " + testUserId);
            stmt.executeUpdate("DELETE FROM Posts WHERE PostID = " + testPostId);
            stmt.executeUpdate("DELETE FROM Subforums WHERE OwnerID = " + testUserId);
            stmt.executeUpdate("DELETE FROM Users WHERE UserID = " + testUserId);
        }
    }

    @Test
    public void testCreateRootComment() throws SQLException {
        Comment comment = commentDAO.createComment("Test comment", testUserId, testPostId);

        assertNotNull(comment);
        assertNotNull(comment.getCommentID());
        assertEquals("Test comment", comment.getCommentText());
        assertEquals(testUserId, (int) comment.getUserID());
        assertEquals(testPostId, (int) comment.getPostID());

        // Verify the comment was actually created in the database
        try (Connection conn = commentDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Comments WHERE CommentID = ?")) {
            stmt.setInt(1, comment.getCommentID());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Test comment", rs.getString("CommentText"));
                assertEquals(testUserId, rs.getInt("UserID"));
                assertEquals(testPostId, rs.getInt("PostID"));
            }
        }
    }

    @Test
    public void testCreateReplyComment() throws SQLException {
        // First create a parent comment
        Comment parent = commentDAO.createComment("Test comment", testUserId, testPostId);
        
        // Then create a reply
        Comment reply = commentDAO.createComment("Reply comment", testUserId, testPostId);
        
        assertNotNull(reply);
        assertNotNull(reply.getCommentID());
        assertEquals("Reply comment", reply.getCommentText());
        assertEquals(testUserId, (int) reply.getUserID());
        assertEquals(testPostId, (int) reply.getPostID());


        // Verify the reply was actually created in the database
        try (Connection conn = commentDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Comments WHERE CommentID = ?")) {
            stmt.setInt(1, reply.getCommentID());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Reply comment", rs.getString("CommentText"));
                assertEquals(testUserId, rs.getInt("UserID"));
                assertEquals(testPostId, rs.getInt("PostID"));
                assertEquals((int)parent.getCommentID(), rs.getInt("ParentID"));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCommentNullText() {
        commentDAO.createComment(null, testUserId, testPostId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCommentNullUserId() {
        commentDAO.createComment("Test", null, testPostId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCommentNullPostId() {
        commentDAO.createComment("Test", testUserId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCommentNullParentId() {
        commentDAO.createComment("Test", testUserId, testPostId);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateCommentSQLException() throws SQLException {
        // Force an SQL exception by violating a constraint
        commentDAO.createComment("Test", -1, -1); // Invalid foreign keys
    }
}