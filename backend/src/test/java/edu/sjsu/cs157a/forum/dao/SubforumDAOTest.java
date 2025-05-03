package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Subforum;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class SubforumDAOTest {

    @Test
    public void test_createSubforum_success() {
        SubforumDAO subforumDAO = new SubforumDAO();
        
        // Create test data
        String name = "Test Subforum " + System.currentTimeMillis();
        String description = "Test description";
        Integer ownerID = 1; // Using existing user ID from sample data
        
        // Test creating subforum
        Subforum createdSubforum = subforumDAO.createSubforum(name, description, Integer.valueOf(ownerID));
        assertNotNull(createdSubforum);
        assertNotNull(createdSubforum.getSubforumID());
        assertEquals(name, createdSubforum.getName());
        assertEquals(description, createdSubforum.getDescription());
        assertEquals(ownerID, createdSubforum.getOwnerID());
        assertEquals("0", createdSubforum.getSubscriberCount());
        assertNotNull(createdSubforum.getCreationDate());
        assertNotNull(createdSubforum.getLastUpdated());
    }

    @Test(expected = SQLException.class)
    public void test_createSubforum_nullName() throws SQLException {
        SubforumDAO subforumDAO = new SubforumDAO();
        try {
            subforumDAO.createSubforum(null, "Test description", 1);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }
            throw e;
        }
    }

    @Test(expected = SQLException.class)
    public void test_createSubforum_emptyName() throws SQLException {
        SubforumDAO subforumDAO = new SubforumDAO();
        try {
            subforumDAO.createSubforum("", "Test description", 1);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }
            throw e;
        }
    }

    @Test(expected = SQLException.class)
    public void test_createSubforum_nullDescription() throws SQLException {
        SubforumDAO subforumDAO = new SubforumDAO();
        try {
            subforumDAO.createSubforum("Test Subforum", null, 1);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createSubforum_nullOwnerID() throws SQLException {
        SubforumDAO subforumDAO = new SubforumDAO();

        subforumDAO.createSubforum("Test Subforum", "Test description", null);

    }
}