package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Subscription;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.*;

public class SubscriptionDAOTest {
    private SubscriptionDAO subscriptionDAO;
    private static final Integer TEST_USER_ID = 1;
    private static final Integer TEST_SUBFORUM_ID = 1;

    @Before
    public void setUp() throws SQLException {
        subscriptionDAO = new SubscriptionDAO();
        // Clear any existing test subscriptions
        try (Connection conn = subscriptionDAO.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM Subscriptions WHERE UserID = " + TEST_USER_ID + 
                              " AND SubforumID = " + TEST_SUBFORUM_ID);
        }
    }

    @Test
    public void test_subscribeUserToSubforum_success() {
        boolean result = subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        assertTrue(result);
        assertTrue(subscriptionDAO.isUserSubscribed(TEST_USER_ID, TEST_SUBFORUM_ID));
    }

    @Test
    public void test_subscribeUserToSubforum_alreadySubscribed() {
        // First subscribe
        subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        // Try to subscribe again
        boolean result = subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void test_subscribeUserToSubforum_nullUserID() {
        subscriptionDAO.subscribeUserToSubforum(null, TEST_SUBFORUM_ID);
    }

    @Test(expected = RuntimeException.class)
    public void test_subscribeUserToSubforum_nullSubforumID() {
        subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, null);
    }

    @Test
    public void test_unsubscribeUserFromSubforum_success() {
        // First subscribe
        subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        // Then unsubscribe
        boolean result = subscriptionDAO.unsubscribeUserFromSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        assertTrue(result);
        assertFalse(subscriptionDAO.isUserSubscribed(TEST_USER_ID, TEST_SUBFORUM_ID));
    }

    @Test
    public void test_unsubscribeUserFromSubforum_notSubscribed() {
        boolean result = subscriptionDAO.unsubscribeUserFromSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void test_unsubscribeUserFromSubforum_nullUserID() {
        subscriptionDAO.unsubscribeUserFromSubforum(null, TEST_SUBFORUM_ID);
    }

    @Test(expected = RuntimeException.class)
    public void test_unsubscribeUserFromSubforum_nullSubforumID() {
        subscriptionDAO.unsubscribeUserFromSubforum(TEST_USER_ID, null);
    }

    @Test
    public void test_isUserSubscribed_true() {
        subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        assertTrue(subscriptionDAO.isUserSubscribed(TEST_USER_ID, TEST_SUBFORUM_ID));
    }

    @Test
    public void test_isUserSubscribed_false() {
        assertFalse(subscriptionDAO.isUserSubscribed(TEST_USER_ID, TEST_SUBFORUM_ID));
    }

    @Test(expected = RuntimeException.class)
    public void test_isUserSubscribed_nullUserID() {
        subscriptionDAO.isUserSubscribed(null, TEST_SUBFORUM_ID);
    }

    @Test(expected = RuntimeException.class)
    public void test_isUserSubscribed_nullSubforumID() {
        subscriptionDAO.isUserSubscribed(TEST_USER_ID, null);
    }

    @Test
    public void test_getUserSubscriptions_success() {
        // Subscribe to test subforum
        subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        
        List<Integer> subscriptions = subscriptionDAO.getUserSubscriptions(TEST_USER_ID);
        assertNotNull(subscriptions);
        assertFalse(subscriptions.isEmpty());
        assertTrue(subscriptions.contains(TEST_SUBFORUM_ID));
    }

    @Test
    public void test_getUserSubscriptions_empty() {
        List<Integer> subscriptions = subscriptionDAO.getUserSubscriptions(999); // Non-existent user
        assertNotNull(subscriptions);
        assertTrue(subscriptions.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void test_getUserSubscriptions_nullUserID() {
        subscriptionDAO.getUserSubscriptions(null);
    }

    @Test
    public void test_getSubforumSubscribers_success() {
        // Subscribe test user
        subscriptionDAO.subscribeUserToSubforum(TEST_USER_ID, TEST_SUBFORUM_ID);
        
        List<Integer> subscribers = subscriptionDAO.getSubforumSubscribers(TEST_SUBFORUM_ID);
        assertNotNull(subscribers);
        assertFalse(subscribers.isEmpty());
        assertTrue(subscribers.contains(TEST_USER_ID));
    }

    @Test
    public void test_getSubforumSubscribers_empty() {
        List<Integer> subscribers = subscriptionDAO.getSubforumSubscribers(999); // Non-existent subforum
        assertNotNull(subscribers);
        assertTrue(subscribers.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void test_getSubforumSubscribers_nullSubforumID() {
        subscriptionDAO.getSubforumSubscribers(null);
    }
}