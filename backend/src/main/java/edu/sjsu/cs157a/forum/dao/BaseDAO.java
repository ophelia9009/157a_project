package edu.sjsu.cs157a.forum.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDAO {


    public Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost:3306/sf_db", "appuser",
                    "Password!1");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Something is wrong", e);
        }

    }
    //each map is a tuple, the table is a list of tuples
    public List<Map<String, Object>> getTable(String table, String[] columns) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT * FROM " + table;
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String col : columns) {
                row.put(col, rs.getObject(col));
            }
            result.add(row);
        }

        rs.close();
        stmt.close();
        return result;
    }

    //List<Map<String, Object>> posts = userDAO.getTableWithCondition("posts", new String[]{"PostID",
    //                            "Title", "BodyText", "CreationDate","Rating", "UserID", "SubforumID"}, null, null);
    // RETURNS SELECT * FROM posts
    // posts = userDAO.getTableWithCondition("posts", new String[]{"PostID",
    //                    "Title", "BodyText", "CreationDate","Rating", "UserID", "SubforumID"}, "PostID < ?", new Object[]{10} );
    // RETURNS SELECT * FROM posts WHERE PostID < 10
    public List<Map<String, Object>> getTableWithCondition(String table, String[] columns, String condition, Object[] parameters) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + table);
        Connection connection = getConnection();

        if(condition != null && !condition.isBlank()) {
            sql.append(" WHERE ").append(condition);
        }

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        if(parameters != null)
            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String col : columns) {
                row.put(col, rs.getObject(col));
            }
            result.add(row);
        }

        rs.close();
        stmt.close();
        return result;
    }

}
