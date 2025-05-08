package edu.sjsu.cs157a.forum.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO extends BaseDAO {
    private static final Logger logger = LogManager.getLogger(SubscriptionDAO.class);
    
    /**
     * Subscribe a user to a subforum
     * @param userID The ID of the user
     * @param subforumID The ID of the subforum
     * @return true if subscription was successful, false otherwise
     */
    public boolean subscribeUserToSubforum(Long userID, Long subforumID) {
        // Check if already subscribed
        if (isUserSubscribed(userID, subforumID)) {
            logger.info("User {} is already subscribed to subforum {}", userID, subforumID);
            return false;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Insert subscription
            stmt = conn.prepareStatement(
                "INSERT INTO Subscriptions (UserID, SubforumID, SubscriptionDate) VALUES (?, ?, ?)"
            );
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setLong(1, userID);
            stmt.setLong(2, subforumID);
            stmt.setTimestamp(3, now);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating subscription failed, no rows affected.");
            }
            
            // Update subscriber count
            updateSubforumSubscriberCount(conn, subforumID, true);
            
            conn.commit();
            return true;
        } catch (SQLException se) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                logger.error("Failed to rollback transaction: {}", e.getMessage());
            }
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to subscribe user to subforum", se);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Failed to close resources: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Unsubscribe a user from a subforum
     * @param userID The ID of the user
     * @param subforumID The ID of the subforum
     * @return true if unsubscription was successful, false otherwise
     */
    public boolean unsubscribeUserFromSubforum(Long userID, Long subforumID) {
        // Check if subscribed
        if (!isUserSubscribed(userID, subforumID)) {
            logger.info("User {} is not subscribed to subforum {}", userID, subforumID);
            return false;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Delete subscription
            stmt = conn.prepareStatement(
                "DELETE FROM Subscriptions WHERE UserID = ? AND SubforumID = ?"
            );
            
            stmt.setLong(1, userID);
            stmt.setLong(2, subforumID);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Unsubscribing failed, no rows affected.");
            }
            
            // Update subscriber count
            updateSubforumSubscriberCount(conn, subforumID, false);
            
            conn.commit();
            return true;
        } catch (SQLException se) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                logger.error("Failed to rollback transaction: {}", e.getMessage());
            }
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to unsubscribe user from subforum", se);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Failed to close resources: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Check if a user is subscribed to a subforum
     * @param userID The ID of the user
     * @param subforumID The ID of the subforum
     * @return true if subscribed, false otherwise
     */
    public boolean isUserSubscribed(Long userID, Long subforumID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT 1 FROM Subscriptions WHERE UserID = ? AND SubforumID = ?"
            );
            
            stmt.setLong(1, userID);
            stmt.setLong(2, subforumID);
            
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException se) {
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to check subscription status", se);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Failed to close resources: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Get all subforums a user is subscribed to
     * @param userID The ID of the user
     * @return List of subforum IDs the user is subscribed to
     */
    public List<Long> getUserSubscriptions(Long userID) {
        List<Long> subscriptions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT SubforumID FROM Subscriptions WHERE UserID = ?"
            );
            
            stmt.setLong(1, userID);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                subscriptions.add(rs.getLong("SubforumID"));
            }
        } catch (SQLException se) {
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to get user subscriptions", se);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Failed to close resources: {}", e.getMessage());
            }
        }
        
        return subscriptions;
    }
    
    /**
     * Get all users subscribed to a subforum
     * @param subforumID The ID of the subforum
     * @return List of user IDs subscribed to the subforum
     */
    public List<Long> getSubforumSubscribers(Long subforumID) {
        List<Long> subscribers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT UserID FROM Subscriptions WHERE SubforumID = ?"
            );
            
            stmt.setLong(1, subforumID);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                subscribers.add(rs.getLong("UserID"));
            }
        } catch (SQLException se) {
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to get subforum subscribers", se);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Failed to close resources: {}", e.getMessage());
            }
        }
        
        return subscribers;
    }
    
    /**
     * Helper method to update subscriber count for a subforum
     * @param conn The database connection
     * @param subforumID The ID of the subforum
     * @param increment Whether to increment (true) or decrement (false) the count
     */
    private void updateSubforumSubscriberCount(Connection conn, Long subforumID, boolean increment) throws SQLException {
        String operation = increment ? "SubscriberCount + 1" : "SubscriberCount - 1";
        String sql = "UPDATE Subforums SET SubscriberCount = " + operation + " WHERE SubforumID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, subforumID);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating subscriber count failed, no rows affected.");
            }
        }
    }
}