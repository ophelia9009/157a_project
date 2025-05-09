package edu.sjsu.cs157a.forum.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import edu.sjsu.cs157a.forum.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDAO extends BaseDAO{
    private static final Logger logger = LogManager.getLogger(UserDAO.class);


    /**
     * This method is to create a new user in database, maily used for user registration.
     * @param newUser
     * @return User
     */
    public User createUser(User newUser){
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Users (Username, Password, Email) VALUES (?, ?, ?)",
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
                    newUser.setUserID(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
            stmt.close();
            conn.close();
            
            return newUser;
        } catch (SQLException se) {
            logger.error("SQL Exception: {}", se.getMessage());
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
                "UPDATE Users SET Username = ?, Password = ?, Email = ? WHERE UserID = ?"
            );
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setLong(4, user.getUserID());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            
            stmt.close();
            conn.close();
            
            return user;
        } catch (SQLException se) {
            logger.error("SQL Exception: {}", se.getMessage());
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
                "DELETE FROM Users WHERE UserID = ?"
            );
            
            stmt.setLong(1, user.getUserID());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
            
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to delete user", se);
        }
    }

    /**
     * This method deletes a user by ID directly
     * @param userId The ID of the user to delete
     * @return boolean indicating if deletion was successful
     */
    public boolean deleteUserById(Long userId) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Users WHERE UserID = ?"
            );
            
            stmt.setObject(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            
            return affectedRows > 0;
        } catch (SQLException se) {
            logger.error("SQL Exception: {}", se.getMessage());
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
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to get password by username", se);
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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE Username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getLong("UserID"),
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
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to get user", se);
        }
        return user;
    }

    /**
     * This method is to fetch user info by userID, mainly used for user profile page.
     * @param userId
     * @return User
     */
    public User getUserById(Long userId) {
        User user = null;
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getLong("UserID"),
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
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to get user", se);
        }
        return user;
    }




    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
            while (rs.next()) {
                users.add(new User(
                    rs.getLong("UserID"),
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
            logger.error("SQL Exception: {}", se.getMessage());
            throw new RuntimeException("Failed to get users", se);
        }
        return users;
    }


    public static void printTable(String table, String[] columns, Connection connection) throws SQLException{
        Logger logger = LogManager.getLogger(UserDAO.class);
        logger.info(table.toUpperCase());
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
        ResultSet rs = statement.executeQuery();
        while(rs.next()) {
            StringBuilder rowData = new StringBuilder();
            for (String column : columns) {
                rowData.append(rs.getString(column)).append(", ");
            }
            logger.info(rowData.toString());
        }
        rs.close();
        statement.close();
    }



}
