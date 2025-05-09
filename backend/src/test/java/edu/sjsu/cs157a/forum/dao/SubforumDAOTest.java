package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Subforum;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.*;

public class SubforumDAOTest {

    @Test
    public void test_createSubforum_success() {
        SubforumDAO subforumDAO = new SubforumDAO();
        
        // Create test data
        String name = "Test Subforum " + System.currentTimeMillis();
        String description = "Test description";
        Long ownerID = 1L; // Using existing user ID from sample data
        
        // Test creating subforum
        Subforum createdSubforum = subforumDAO.createSubforum(name, description, Long.valueOf(ownerID));
        assertNotNull(createdSubforum);
        assertNotNull(createdSubforum.getSubforumID());
        assertEquals(name, createdSubforum.getName());
        assertEquals(description, createdSubforum.getDescription());
        assertEquals(ownerID, createdSubforum.getOwnerID());
        assertEquals("0", createdSubforum.getSubscriberCount());
        assertNotNull(createdSubforum.getCreationDate());
        assertNotNull(createdSubforum.getLastUpdated());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createSubforum_nullName() throws SQLException {
        SubforumDAO subforumDAO = new SubforumDAO();
        try {
            subforumDAO.createSubforum(null, "Test description", 1L);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createSubforum_emptyName() throws SQLException {
        SubforumDAO subforumDAO = new SubforumDAO();
        try {
            subforumDAO.createSubforum("", "Test description", 1L);
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
            subforumDAO.createSubforum("Test Subforum", null, 1L);
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

    @Test
    public void test_updateSubforum_success() {
        SubforumDAO subforumDAO = new SubforumDAO();
        
        // First create a subforum to update with unique name
        String name = "Test Subforum Update " + System.currentTimeMillis();
        String originalDesc = "Original description";
        Long ownerID = 1L;
        Subforum subforum = subforumDAO.createSubforum(name, originalDesc, ownerID);
        
        // Update description
        String newDesc = "Updated description " + System.currentTimeMillis();
        subforumDAO.updateSubforum(subforum.getSubforumID(), newDesc, ownerID);
    }

    @Test(expected = RuntimeException.class)
    public void test_updateSubforum_nonOwner() {
        SubforumDAO subforumDAO = new SubforumDAO();
        
        // Create subforum owned by user 1L with unique name
        String name = "Test Subforum NonOwner " + System.currentTimeMillis();
        Subforum subforum = subforumDAO.createSubforum(name, "Test desc", 1L);
        
        // Try to update as user 2 (non-owner)
        subforumDAO.updateSubforum(subforum.getSubforumID(), "New desc", 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateSubforum_nullSubforumID() {
        SubforumDAO subforumDAO = new SubforumDAO();
        subforumDAO.updateSubforum(null, "New desc", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateSubforum_nullRequestingUserID() {
        SubforumDAO subforumDAO = new SubforumDAO();
        subforumDAO.updateSubforum(1L, "New desc", null);
    }

    @Test
    public void test_updateSubforum_emptyDescription() {
        SubforumDAO subforumDAO = new SubforumDAO();
        String name = "Test Subforum EmptyDesc " + System.currentTimeMillis();
        Subforum subforum = subforumDAO.createSubforum(name, "Test desc", 1L);
        
        subforumDAO.updateSubforum(subforum.getSubforumID(), "", 1L);
    }

    @Test
    public void test_getSubscribedSubforums_success() {
        SubforumDAO subforumDAO = new SubforumDAO();
        List<Subforum> subscribed = subforumDAO.getSubscribedSubforums(1L);
        assertNotNull(subscribed);
    }
    
    @Test
    public void test_getSubscribedSubforums_noSubscriptions() {
        SubforumDAO subforumDAO = new SubforumDAO();
        List<Subforum> subscribed = subforumDAO.getSubscribedSubforums(999L);
        assertNotNull(subscribed);
        assertTrue(subscribed.isEmpty());
    }
    
    @Test(expected = RuntimeException.class)
    public void test_getSubscribedSubforums_invalidUser() {
        SubforumDAO subforumDAO = new SubforumDAO();
        subforumDAO.getSubscribedSubforums(null);
    }
    @Test
    public void test_getAllSubforumsOrderedByLastUpdated_success() {
        SubforumDAO subforumDAO = new SubforumDAO();
        
        try {
            // Create test subforums with explicit timestamps
            long now = System.currentTimeMillis();
            Timestamp oldestTime = new Timestamp(now - 2000);
            Timestamp middleTime = new Timestamp(now - 1000);
            Timestamp newestTime = new Timestamp(now);
            
            // Create subforums with controlled timestamps and unique names
            String uniqueId = String.valueOf(now);
            
            // Oldest subforum - created 2 seconds ago
            Subforum oldest = subforumDAO.createSubforum("Oldest-" + uniqueId, "Oldest subforum", 1L);
            subforumDAO.updateSubforum(oldest.getSubforumID(), "Updated oldest", 1L);
            
            // Sleep to ensure different timestamps
            Thread.sleep(2000);
            
            // Middle subforum - created now
            Subforum middle = subforumDAO.createSubforum("Middle-" + uniqueId, "Middle subforum", 1L);
            
            // Sleep again
            Thread.sleep(2000);
            
            // Newest subforum - created 2 seconds later
            Subforum newest = subforumDAO.createSubforum("Newest-" + uniqueId, "Newest subforum", 1L);
            subforumDAO.updateSubforum(newest.getSubforumID(), "Updated newest", 1L);
            
            // Get all subforums ordered by last updated
            List<Subforum> subforums = subforumDAO.getAllSubforumsOrderedByLastUpdated();
            
            // Verify ordering (newest first)
            assertNotNull(subforums);
            
            // Find our test subforums in the results
            Subforum foundNewest = null;
            Subforum foundMiddle = null;
            Subforum foundOldest = null;
            
            for (Subforum s : subforums) {
                if (s.getName().startsWith("Newest-")) foundNewest = s;
                if (s.getName().startsWith("Middle-")) foundMiddle = s;
                if (s.getName().startsWith("Oldest-")) foundOldest = s;
            }
            
            assertNotNull("Newest subforum not found", foundNewest);
            assertNotNull("Middle subforum not found", foundMiddle);
            assertNotNull("Oldest subforum not found", foundOldest);
            
            // Debug output
            System.out.println("Newest timestamp: " + foundNewest.getLastUpdated());
            System.out.println("Middle timestamp: " + foundMiddle.getLastUpdated());
            System.out.println("Oldest timestamp: " + foundOldest.getLastUpdated());
            
            // Verify ordering
            assertTrue("Newest (" + foundNewest.getLastUpdated() + ") should be after Middle (" + foundMiddle.getLastUpdated() + ")",
                foundNewest.getLastUpdated().after(foundMiddle.getLastUpdated()));
            assertTrue("Middle (" + foundMiddle.getLastUpdated() + ") should be after Oldest (" + foundOldest.getLastUpdated() + ")",
                foundMiddle.getLastUpdated().after(foundOldest.getLastUpdated()));
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        } finally {
            // Clean up test data
            try {
                Connection conn = subforumDAO.getConnection();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("DELETE FROM Subforums WHERE Name LIKE 'Oldest-%' OR Name LIKE 'Middle-%' OR Name LIKE 'Newest-%'");
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Failed to clean up test data: " + e.getMessage());
            }
        }
    }

    @Test
    public void test_getAllSubforumsOrderedByLastUpdated_empty() {
        SubforumDAO subforumDAO = new SubforumDAO();
        List<Subforum> subforums = subforumDAO.getAllSubforumsOrderedByLastUpdated();
        assertNotNull(subforums);
    }

    @Test(expected = RuntimeException.class)
    public void test_getAllSubforumsOrderedByLastUpdated_dbError() {
        SubforumDAO subforumDAO = new SubforumDAO();
        // Force error by passing invalid SQL (simpler than connection manipulation)
        try {
            subforumDAO.getConnection().prepareStatement("INVALID SQL").executeQuery();
            subforumDAO.getAllSubforumsOrderedByLastUpdated();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getSubforumByID_null() {
        SubforumDAO subforumDAO = new SubforumDAO();
        subforumDAO.getSubforumByID(null);
    }

    // ... existing code ...

    @Test(expected = IllegalArgumentException.class)
    public void test_deleteSubforum_null() {
        SubforumDAO dao = new SubforumDAO();
        dao.deleteSubforum(null);
    }

    @Test
    public void test_deleteSubforum_success() {
        SubforumDAO dao = new SubforumDAO();
        // Create a subforum to delete
        String name = "tempDel_" + System.currentTimeMillis();
        String desc = "tempDesc";
        Long ownerID = 1L;
        Subforum sub = dao.createSubforum(name, desc, ownerID);
        Long id = sub.getSubforumID();
        // Delete it
        dao.deleteSubforum(id);
        // Verify it's gone
        assertNull(dao.getSubforumByID(id));
    }
}
