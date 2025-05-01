package com.example.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import com.example.model.User;

public class UserDAO extends BaseDAO{


    /**
     * This method is to create a new user in database, maily used for user registeration.
     * @param newUser
     * @return
     */
    public User createUser(User newUser){
        // 
        return newUser;
    }


    /**
     * This method is to update user, mainly used for user file edit.
     * @param user
     * @return
     */
    public User updateUser(User user){
        return user;
    }

    /**
     * This method is to delete user, maily used for user deletion.
     * @param user
     */

    public void deleteUser(User user){
        
    }
    

    /**
     * This method is to fetch user password, mainly used for user login.
     * @param username
     * @return
     */
    public String getPasswordByUsername(String username) {

        String result = null;
        try {
            Connection conn = getConnection();
            // Create and execute SQL statement (Step 3)
            PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Users where Username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            // Display the SQL query results
            if (rs.next()) {
                result = rs.getString("Password");
            }
            // Make sure our DB resources are released (step 4) in reverse order
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("");
        }

        return  result;


    }

    /**
     * This method is to fetch user info by userID, mainly used for user profile page.
     * @param userId
     * @return
     */
    public User getUserById(String userId) {
        User user = null;
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE UserID = ?");
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    gStr(rs, "UserID"),
                    gStr(rs, "Username"),
                    gStr(rs, "Password"),
                    gStr(rs, "Email"),
                    rs.getTimestamp("RegisterDate")
                );
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to get user", se);
        }
        return user;
    }

    


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                users.add(new User(
                    gStr(rs, "UserID"),
                    gStr(rs, "Username"),
                    gStr(rs, "Password"),
                    gStr(rs, "Email"),
                    rs.getTimestamp("RegisterDate")
                ));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to get users", se);
        }
        return users;
    }

    public static String gStr(ResultSet rs, String s) {
        try {
            return rs.getString(s);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/sf_db",
                    "root",
                    "1234"
            );
            Statement statement = connection.createStatement();
            System.out.println("USERS");
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println(gStr(rs, "UserID") + ", " + gStr(rs, "Username") + ", " +
                        gStr(rs, "Password") + ", " + gStr(rs, "Email") + ", " + gStr(rs, "RegisterDate"));
            }
            System.out.println("SUBFORUMS");
            Statement statement2 = connection.createStatement();
            rs = statement2.executeQuery("SELECT * FROM subforums");
            while (rs.next()) {
                System.out.println(gStr(rs, "SubforumID") + ", " + gStr(rs, "Name") + ", " +
                        gStr(rs, "Description") + ", " + gStr(rs, "CreationDate") + ", " +
                        gStr(rs, "SubscriberCount") + ", " + gStr(rs, "LastUpdated"));
            }
            System.out.println("POSTS");
            Statement statement3 = connection.createStatement();
            rs = statement3.executeQuery("SELECT * FROM posts");
            while (rs.next()) {
                System.out.println(gStr(rs, "PostID") + ", " + gStr(rs, "Title") + ", " + gStr(rs, "BodyText")
                        + ", " + gStr(rs, "CreationDate") + ", " + gStr(rs, "Rating") + ", " +
                        gStr(rs, "UserID") + ", " + gStr(rs, "SubforumID"));
            }
            System.out.println("COMMENTS");
            Statement statement4 = connection.createStatement();
            rs = statement4.executeQuery("SELECT * FROM comments");
            while (rs.next()) {
                System.out.println(gStr(rs, "CommentID") + ", " + gStr(rs, "CommentText") + ", " +
                        gStr(rs, "CreationDate") + ", " + gStr(rs, "Rating") + ", " + gStr(rs, "UserID") + ", "
                        + gStr(rs, "PostID") + ", " + gStr(rs, "ParentID") + ", " + gStr(rs, "LastUpdated"));
            }
            System.out.println("SUBSCRIPTIONS");
            Statement statement5 = connection.createStatement();
            rs = statement5.executeQuery("SELECT * FROM subscriptions");
            while (rs.next()) {
                System.out.println(gStr(rs, "UserID") + ", " + gStr(rs, "SubforumID") + ", " +
                        gStr(rs, "SubscriptionDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
