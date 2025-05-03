package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Post;
import edu.sjsu.cs157a.forum.model.Subforum;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubforumDAO extends BaseDAO {
    public List<Post> getAllSubforumPosts (Integer subforumID) {
        List<Post> posts = new ArrayList<>();

        String sql = "SELECT * FROM posts WHERE SubforumID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, subforumID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("PostID"),
                        rs.getString("Title"),
                        rs.getString("BodyText"),
                        rs.getTimestamp("CreationDate"),
                        rs.getInt("Rating"),
                        rs.getInt("UserID"),
                        rs.getInt("SubforumID")
                ));
            }
            rs.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to get posts", se);
        }

        return posts;
    }

    /**
     * get a filtered list of subforums
     * @param filterName filter by keyword? maybe make this a list of keywords later
     * @param minCreationDate ...
     * @return the list of subforums
     */
    public List<Subforum> getFilteredSubforums(String filterName, Timestamp minCreationDate, Timestamp maxCreationDate,
    BigInteger minSubscriberCount, BigInteger maxSubscriberCount, Timestamp minLastUpdated, Timestamp maxLastUpdated) {
        List<Subforum> subforums = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM subforums WHERE 1=1");
        if (filterName != null && !filterName.isEmpty()) {
            sql.append(" AND Name LIKE '%").append(filterName).append("%'");
        }
        if (minCreationDate != null) {
            sql.append(" AND CreationDate >= '").append(minCreationDate).append("'");
        }
        if (maxCreationDate != null) {
            sql.append(" AND CreationDate <= '").append(maxCreationDate).append("'");
        }
        if (minSubscriberCount != null) {
            sql.append(" AND SubscriberCount >= ").append(minSubscriberCount);
        }
        if (maxSubscriberCount != null) {
            sql.append(" AND SubscriberCount <= ").append(maxSubscriberCount);
        }
        if (minLastUpdated != null) {
            sql.append(" AND LastUpdated >= '").append(minLastUpdated).append("'");
        }
        if (maxLastUpdated != null) {
            sql.append(" AND LastUpdated <= '").append(maxLastUpdated).append("'");
        }

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subforums.add(new Subforum(
                        rs.getInt("SubforumID"),
                        rs.getString("Name"),
                        rs.getTimestamp("CreationDate"),
                        rs.getString("Description"),
                        rs.getString("SubscriberCount"),
                        rs.getTimestamp("LastUpdated"),
                        rs.getInt("OwnerID")
                ));
            }
            rs.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to get subforums", se);
        }
        return subforums;
    }

    /**
     * Creates a new subforum
     * @param name The name of the subforum
     * @param description The description of the subforum
     * @param ownerID The ID of the user creating the subforum
     * @return The created Subforum object
     * @throws RuntimeException if subforum creation fails
     */
    public Subforum createSubforum(String name, String description, Integer ownerID) {
        if (ownerID == null)
            throw new IllegalArgumentException("ownerID cannot be null");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO subforums (Name, CreationDate, Description, SubscriberCount, LastUpdated, OwnerID) " +
                     "VALUES (?, ?, ?, 0, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setTimestamp(2, now);
            stmt.setString(3, description);
            stmt.setTimestamp(4, now);
            stmt.setInt(5, ownerID);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating subforum failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer subforumID = generatedKeys.getInt(1);
                    return new Subforum(subforumID, name, now, description, "0", now, ownerID);
                } else {
                    throw new SQLException("Creating subforum failed, no ID obtained.");
                }
            }
        } catch (SQLException se) {
            System.out.println("SQL ErrorState: " + se.getSQLState());
            System.out.println("SQL ErrorCode: " + se.getErrorCode());
            se.printStackTrace();
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to create subforum", se);
        }
    }

    /**
     * Updates a subforum's description if the requesting user is the owner
     * @param subforumID The ID of the subforum to update
     * @param newDescription The new description for the subforum
     * @param requestingUserID The ID of the user requesting the update
     * @return The updated Subforum object
     * @throws RuntimeException if update fails or user is not owner
     */
    public void updateSubforum(Integer subforumID, String newDescription, Integer requestingUserID) {
        if (subforumID == null || requestingUserID == null) {
            throw new IllegalArgumentException("subforumID and requestingUserID cannot be null");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());


        // Update description
        String updateSql = "UPDATE subforums SET Description = ?, LastUpdated = ? " +
                         "WHERE SubforumID = ? AND OwnerID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, newDescription);
            stmt.setTimestamp(2, now);
            stmt.setInt(3, subforumID);
            stmt.setInt(4, requestingUserID);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Update failed - subforum not found or user is not owner");
            }

        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to update subforum", se);
        }
    }

    /**
     * Gets all subforums that a user is subscribed to
     * @param userID The ID of the user
     * @return List of subscribed subforums
     * @throws RuntimeException if database operation fails
     */
    public List<Subforum> getSubscribedSubforums(Integer userID) {
        List<Subforum> subscribedForums = new ArrayList<>();

        String query = "SELECT s.* FROM subforums s " +
                     "JOIN subscriptions sub ON s.SubforumID = sub.SubforumID " +
                     "WHERE sub.UserID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subscribedForums.add(new Subforum(
                    rs.getInt("SubforumID"),
                    rs.getString("Name"),
                    rs.getTimestamp("CreationDate"),
                    rs.getString("Description"),
                    rs.getString("SubscriberCount"),
                    rs.getTimestamp("LastUpdated"),
                    rs.getInt("OwnerID")
                ));
            }
            rs.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to get subscribed subforums", se);
        }
        return subscribedForums;
    }

    /**
     * Gets all subforums ordered by last updated timestamp (newest first)
     * @return List of all subforums ordered by LastUpdated
     * @throws RuntimeException if database operation fails
     */
    public List<Subforum> getAllSubforumsOrderedByLastUpdated() {
        List<Subforum> subforums = new ArrayList<>();
        String sql = "SELECT * FROM subforums ORDER BY LastUpdated DESC";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subforums.add(new Subforum(
                        rs.getInt("SubforumID"),
                        rs.getString("Name"),
                        rs.getTimestamp("CreationDate"),
                        rs.getString("Description"),
                        rs.getString("SubscriberCount"),
                        rs.getTimestamp("LastUpdated"),
                        rs.getInt("OwnerID")
                ));
            }
            rs.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to get subforums", se);
        }
        return subforums;
    }
}
