package edu.sjsu.cs157a.forum.dao;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.sjsu.cs157a.forum.dao.PostDAO;
import edu.sjsu.cs157a.forum.model.Post;
import java.sql.Timestamp;
import java.math.BigInteger;
import java.util.List;

public class PostDAOTest {

    private PostDAO postDAO = new PostDAO();

    @Test(expected = IllegalArgumentException.class)
    public void test_getPostByID_null() {
        postDAO.getPostByID(null);
    }

    @Test
    public void test_getPostByID_existing() {
        assertNotNull(postDAO.getPostByID(1L));
    }

    @Test
    public void test_getPostByID_nonexistent() {
        assertNull(postDAO.getPostByID(999999L));
    }

    @Test
    public void test_deletePost_null() {
        // expecting IllegalArgumentException
        postDAO.deletePost(null);
    }

    @Test
    public void test_deletePost_success() {
        // Create a post to delete
        Post created = postDAO.createPost("tempTitle_" + System.currentTimeMillis(), "tempBody", 1L, 1L);
        assertNotNull(created.getPostID());
        boolean deleted = postDAO.deletePost(created.getPostID());
        assertTrue(deleted);
        assertNull(postDAO.getPostByID(created.getPostID()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createPost_blankTitle() {
        postDAO.createPost("", "body", 1L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createPost_blankBody() {
        postDAO.createPost("title", "", 1L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createPost_nullUserID() {
        postDAO.createPost("title", "body", null, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createPost_nullSubforumID() {
        postDAO.createPost("title", "body", 1L, null);
    }

    @Test
    public void test_createPost_success() {
        String title = "testTitle_" + System.currentTimeMillis();
        String body = "testBody";
        Post created = postDAO.createPost(title, body, 1L, 1L);
        assertNotNull(created);
        assertNotNull(created.getPostID());
        assertEquals(title, created.getTitle());
        assertEquals(body, created.getBodyText());
        // Clean up
        postDAO.deletePost(created.getPostID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_editPost_nullPost() {
        postDAO.editPost(null, "newTitle", "newBody");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_editPost_blankBoth() {
        Post existing = postDAO.getPostByID(1L);
        postDAO.editPost(existing, "", "");
    }

    @Test
    public void test_editPost_success() {
        // Create a post to edit
        Post created = postDAO.createPost("origTitle_" + System.currentTimeMillis(), "origBody", 1L, 1L);
        // Edit title only
        postDAO.editPost(created, "newTitle", "");
        Post updated = postDAO.getPostByID(created.getPostID());
        assertEquals("newTitle", updated.getTitle());
        assertEquals("origBody", updated.getBodyText());
        // Clean up
        postDAO.deletePost(created.getPostID());
    }

    @Test
    public void test_getFilteredPosts() {
        // Create unique post for filtering
        String unique = "flt_" + System.currentTimeMillis();
        Post created = postDAO.createPost(unique, "body", 1L, 1L);
        List<Post> list = postDAO.getFilteredPosts(unique, null, null, null, null, null, null, 1L);
        assertNotNull(list);
        assertTrue(list.stream().anyMatch(p -> p.getPostID().equals(created.getPostID())));
        // Clean up
        postDAO.deletePost(created.getPostID());
    }
} 