package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Post;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostDAO extends BaseDAO{
    public Post getPostByPostID(Integer postID){
        try {
            Map<String, Object> lm = findByPrimaryKey("posts", "PostID", postID);
            return new Post((Integer) lm.get("PostID"), (String) lm.get("Title"), (String) lm.get("BodyText"),
                    (Timestamp) lm.get("CreationDate"), (Integer) lm.get("Rating"), (Integer) lm.get("UserID"),
                    (Integer) lm.get("SubforumID"), (Timestamp) lm.get("LastUpdated"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Post createPost(String title, String bodyText, Integer userID, Integer subforumID){
        if (title.isBlank())
            throw new IllegalArgumentException("title cannot be blank for post creation");
        if (bodyText.isBlank())
            throw new IllegalArgumentException("bodyText cannot be blank for post creation");
        if (userID == null)
            throw new IllegalArgumentException("userID cannot be null for post creation");
        if (subforumID == null)
            throw new IllegalArgumentException("subforumID cannot be null for post creation");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO posts (Title, BodyText, CreationDate, Rating, UserID, SubforumID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, title);
            stmt.setString(2, bodyText);
            stmt.setTimestamp(3, now);
            stmt.setInt(4, 0);
            stmt.setInt(4, userID);
            stmt.setInt(5, subforumID);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating post failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer postID = generatedKeys.getInt(1);
                    return new Post(postID, title, bodyText, now, 0, userID, subforumID, now);
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
        } catch (SQLException se) {
            System.out.println("SQL ErrorState: " + se.getSQLState());
            System.out.println("SQL ErrorCode: " + se.getErrorCode());
            se.printStackTrace();
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to create post", se);
        }
    }
    public Post editPost(Post post, String newTitle, String newBodyText){
        if (post == null)
            throw new IllegalArgumentException("post cannot be null for post updating");
        if (newTitle.isBlank() && newBodyText.isBlank())
            throw new IllegalArgumentException("title and bodyText cannot both be blank for post updating");
        String sql = "UPDATE posts SET Title = ?, BodyText = ?  WHERE PostID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(!newTitle.isBlank())
                stmt.setString(1, newTitle);
            else
                stmt.setString(1, post.getTitle());
            if(!newBodyText.isBlank())
                stmt.setString(2, newBodyText);
            else
                stmt.setString(2, post.getBodytext());
            stmt.setInt(3, post.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating post failed, no rows affected.");
            }
            return post;
            }
         catch (SQLException se) {
            System.out.println("SQL ErrorState: " + se.getSQLState());
            System.out.println("SQL ErrorCode: " + se.getErrorCode());
            se.printStackTrace();
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to update post", se);
        }
    }
    public boolean deletePost(Integer postID){
        if (postID == null)
            throw new IllegalArgumentException("postID cannot be null for post deletion");

        String sql = "DELETE FROM posts WHERE postID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, postID);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting post failed, no rows affected.");
            }

            stmt.close();
            conn.close();
            return true;
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to post user", se);
        }
    }
    /**
     * get a filtered list of subforums
     * @param filterName filter by keyword? maybe make this a list of keywords later
     * @param minCreationDate ...
     * @return the list of subforums
     */
    public List<Post> getFilteredPosts(String filterName, Timestamp minCreationDate, Timestamp maxCreationDate,
        BigInteger minSubscriberCount, BigInteger maxSubscriberCount, Timestamp minLastUpdated, Timestamp maxLastUpdated,
                                       Integer SubforumID) {
        List<Post> posts = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM posts WHERE ");
        if(SubforumID != null)
            sql.append("SubforumID = ").append(SubforumID);
        else
            sql.append("1 = 1");
        if (filterName != null && !filterName.isEmpty()) {
            sql.append(" AND Title LIKE '%").append(filterName).append("%'");
        }
        if (minCreationDate != null) {
            sql.append(" AND CreationDate >= '").append(minCreationDate).append("'");
        }
        if (maxCreationDate != null) {
            sql.append(" AND CreationDate <= '").append(maxCreationDate).append("'");
        }
        if (minSubscriberCount != null) {
            sql.append(" AND Rating >= ").append(minSubscriberCount);
        }
        if (maxSubscriberCount != null) {
            sql.append(" AND Rating <= ").append(maxSubscriberCount);
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
                posts.add(new Post(
                        rs.getInt("PostID"),
                        rs.getString("Title"),
                        rs.getString("BodyText"),
                        rs.getTimestamp("CreationDate"),
                        rs.getInt("Rating"),
                        rs.getInt("UserID"),
                        rs.getInt("SubforumID"),
                        rs.getTimestamp("LastUpdated")
                ));
            }
            rs.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to get posts when trying to list/filter them", se);
        }
        return posts;
    }
}
