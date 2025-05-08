package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Comment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;
import static org.junit.Assert.*;

public class CommentDAOTest {
    private CommentDAO commentDAO;
    private Long testUserId;
    private Long testPostId;

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
                testUserId = rs.getLong(1);
            }

            // Create test subforum
            String randomSubforum = "testsub_" + java.util.UUID.randomUUID().toString().substring(0, 8);
            stmt.executeUpdate("INSERT INTO Subforums (Name, Description, OwnerID) VALUES ('" + randomSubforum + "', 'test desc', " + testUserId + ")");
            Long subforumId;
            try (ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                subforumId = rs.getLong(1);
            }

            // Create test post
            stmt.executeUpdate("INSERT INTO Posts (Title, BodyText, UserID, SubforumID) VALUES ('test post', 'test content', " + testUserId + ", " + subforumId + ")");
            try (ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                testPostId = rs.getLong(1);
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
        assertEquals(testUserId, (Long) comment.getUserID());
        assertEquals(testPostId, (Long) comment.getPostID());

        // Verify the comment was actually created in the database
        try (Connection conn = commentDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Comments WHERE CommentID = ?")) {
            stmt.setLong(1, comment.getCommentID());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Test comment", rs.getString("CommentText"));
                assertEquals(testUserId.longValue(), rs.getLong("UserID"));
                assertEquals(testPostId.longValue(), rs.getLong("PostID"));
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
        assertEquals(testUserId, (Long) reply.getUserID());
        assertEquals(testPostId, (Long) reply.getPostID());

        // Verify the reply was actually created in the database
        try (Connection conn = commentDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Comments WHERE CommentID = ?")) {
            stmt.setLong(1, reply.getCommentID());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Reply comment", rs.getString("CommentText"));
                assertEquals(testUserId.longValue(), rs.getLong("UserID"));
                assertEquals(testPostId.longValue(), rs.getLong("PostID"));
                //assertEquals((Long)parent.getCommentID(), rs.getLong("ParentID"));
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

    @Test(expected = RuntimeException.class)
    public void testCreateCommentSQLException() throws SQLException {
        // Force an SQL exception by violating a constraint
        commentDAO.createComment("Test", -1L, -1L); // Invalid foreign keys
    }

    @Test
    public void testUpdateComment() throws SQLException {
        // Create test comment
        Comment comment = commentDAO.createComment("Original text", testUserId, testPostId);
        
        // Update the comment
        Comment updated = commentDAO.updateComment(comment.getCommentID(), testUserId, "Updated text");
        
        assertNotNull(updated);
        assertEquals(comment.getCommentID(), updated.getCommentID());
        assertEquals("Updated text", updated.getCommentText());
        assertEquals(testUserId, (Long) updated.getUserID());
        assertEquals(testPostId, (Long) updated.getPostID());
        assertTrue(updated.getLastUpdated().after(comment.getLastUpdated()));

        // Verify the update in database
        try (Connection conn = commentDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Comments WHERE CommentID = ?")) {
            stmt.setLong(1, comment.getCommentID());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Updated text", rs.getString("CommentText"));

            }
        }
    }

    @Test(expected = SQLException.class)
    public void testUpdateNonExistentComment() throws SQLException {
        commentDAO.updateComment(-1L, testUserId, "New text");
    }

    @Test(expected = SQLException.class)
    public void testUpdateCommentWrongUser() throws SQLException {
        Comment comment = commentDAO.createComment("Original text", testUserId, testPostId);
        commentDAO.updateComment(comment.getCommentID(), testUserId + 1, "New text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCommentNullCommentId() throws SQLException {
        commentDAO.updateComment(null, testUserId, "New text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCommentNullUserId() throws SQLException {
        commentDAO.updateComment(1L, null, "New text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCommentNullText() throws SQLException {
        commentDAO.updateComment(1L, testUserId, null);
    }

    @Test
    public void testGetCommentsByPost() throws SQLException, InterruptedException {
        // Create test comments
        Comment comment1 = commentDAO.createComment("Comment 1", testUserId, testPostId);
        Thread.sleep(1000); // Ensure different timestamps
        Comment comment2 = commentDAO.createComment("Comment 2", testUserId, testPostId);

        // Get comments
        List<Comment> comments = commentDAO.getCommentsByPost(testPostId);

        // Verify results
        assertEquals(2, comments.size());
        
        // Should be ordered by CreationDate DESC (newest first)
        assertEquals(comment2.getCommentID(), comments.get(0).getCommentID());
        assertEquals("Comment 2", comments.get(0).getCommentText());
        assertEquals(comment1.getCommentID(), comments.get(1).getCommentID());
        assertEquals("Comment 1", comments.get(1).getCommentText());

        // Verify all fields are populated correctly
        for (Comment c : comments) {
            assertNotNull(c.getCommentID());
            assertNotNull(c.getCommentText());
            assertNotNull(c.getCreationDate());
            assertNotNull(c.getUserID());
            assertNotNull(c.getPostID());
            assertNotNull(c.getLastUpdated());
        }
    }

    @Test
    public void testGetCommentsByPostEmpty() throws SQLException {
        List<Comment> comments = commentDAO.getCommentsByPost(testPostId);
        assertTrue(comments.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCommentsByPostNullPostId() throws SQLException {
        commentDAO.getCommentsByPost(null);
    }

    @Test
    public void testDeleteComment() throws SQLException {
        // Create test comment
        Comment comment = commentDAO.createComment("Test comment", testUserId, testPostId);
        
        // Delete the comment
        boolean deleted = commentDAO.deleteComment(comment.getCommentID(), testUserId);
        
        assertTrue(deleted);
        
        // Verify the comment was actually deleted
        try (Connection conn = commentDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Comments WHERE CommentID = ?")) {
            stmt.setLong(1, comment.getCommentID());
            try (ResultSet rs = stmt.executeQuery()) {
                assertFalse(rs.next());
            }
        }
    }

    @Test
    public void testDeleteCommentNotFound() throws SQLException {
        // Try to delete non-existent comment
        boolean deleted = commentDAO.deleteComment(-1L, testUserId);
        assertFalse(deleted);
    }

    @Test
    public void testDeleteCommentWrongUser() throws SQLException {
        // Create test comment
        Comment comment = commentDAO.createComment("Test comment", testUserId, testPostId);
        
        // Try to delete with wrong user
        boolean deleted = commentDAO.deleteComment(comment.getCommentID(), testUserId + 1);
        assertFalse(deleted);
        
        // Verify comment still exists
        try (Connection conn = commentDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Comments WHERE CommentID = ?")) {
            stmt.setLong(1, comment.getCommentID());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteCommentNullCommentId() throws SQLException {
        commentDAO.deleteComment(null, testUserId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteCommentNullUserId() throws SQLException {
        commentDAO.deleteComment(1L, null);
    }
}