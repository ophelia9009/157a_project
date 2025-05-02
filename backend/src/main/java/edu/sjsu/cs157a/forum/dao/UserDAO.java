package edu.sjsu.cs157a.forum.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import edu.sjsu.cs157a.forum.model.User;

public class UserDAO extends BaseDAO{


    /**
     * This method is to create a new user in database, maily used for user registration.
     * @param newUser
     * @return User
     */
    public User createUser(User newUser){
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (Username, Password, Email) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            stmt.setString(1, newUser.getUsername());
            stmt.setString(2, newUser.getPassword());
            stmt.setString(3, newUser.getEmail());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newUser.setUserID(generatedKeys.getString(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
            stmt.close();
            conn.close();
            
            return newUser;
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to create user", se);
        }
    }


    /**
     * This method is to update user, mainly used for user file edit.
     * @param user
     * @return User
     */
    public User updateUser(User user){
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE users SET Username = ?, Password = ?, Email = ? WHERE UserID = ?"
            );
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUserID());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            
            stmt.close();
            conn.close();
            
            return user;
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to update user", se);
        }
    }

    /**
     * This method is to delete user, maily used for user deletion.
     * @param user
     */

    public void deleteUser(User user){
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM users WHERE UserID = ?"
            );
            
            stmt.setString(1, user.getUserID());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
            
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to delete user", se);
        }
    }

    /**
     * This method deletes a user by ID directly
     * @param userId The ID of the user to delete
     * @return boolean indicating if deletion was successful
     */
    public boolean deleteUserById(String userId) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM users WHERE UserID = ?"
            );
            
            stmt.setString(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            
            return affectedRows > 0;
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to delete user", se);
        }
    }


    /**
     * This method is to fetch user password, mainly used for user login.
     * @param username
     * @return String
     */
    public String getPasswordByUsername(String username) {
        String result = null;
        try {
            Connection conn = getConnection();
            // Create and execute SQL statement (Step 3)
            PreparedStatement stmt = conn.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
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
     * This method is to fetch user by username, mainly used for login.
     * @param username
     * @return User
     */
    public User getUserByUsername(String username) {
        User user = null;
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE Username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getString("UserID"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Email"),
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

    /**
     * This method is to fetch user info by userID, mainly used for user profile page.
     * @param userId
     * @return User
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
                    rs.getString("UserID"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Email"),
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
                    rs.getString("UserID"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Email"),
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


    public static void printTable(String table, String[] columns, Connection connection) throws SQLException{
        System.out.println(table.toUpperCase());
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
        ResultSet rs = statement.executeQuery();
        while(rs.next()) {
            for (String column : columns) {
                System.out.print(rs.getString(column) + ", ");
            }
            System.out.println();
        }
        rs.close();
        statement.close();
    }
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/sf_db",
                    "root",
                    "1234"
            );

            printTable("users", new String[]{"UserID", "Username", "Password", "Email", "RegisterDate"},
                    connection);
            printTable("subforums", new String[]{"SubforumID", "Name", "Description", "CreationDate",
                    "SubscriberCount", "LastUpdated"}, connection);
            printTable("posts", new String[]{"PostID", "Title", "BodyText", "CreationDate", "Rating", "UserID",
                    "SubforumID"}, connection);
            printTable("comments", new String[]{"CommentID", "CommentText", "CreationDate", "Rating",
                    "UserID", "PostID", "ParentID", "LastUpdated"}, connection);
            printTable("subscriptions", new String[]{"UserID", "SubforumID", "SubscriptionDate"}, connection);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
