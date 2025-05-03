package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Element;

import java.sql.*;
import java.util.*;

public class BaseDAO {

    public static final List <String> VALID_TABLES = Arrays.asList("users", "subforums", "posts",
     "comments", "subscriptions");
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
    //with limit
    public List<Map<String, Object>> getTable(String table, String[] columns, int limit) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT " + String.join(", ", columns) + " FROM " + table + " LIMIT " + limit;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (String col : columns) {
                    row.put(col, rs.getObject(col));
                }
                result.add(row);
            }
        }
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
    /**
     * This method is to create a new tuple in database, mainly used for user registration.
     * @param newElement
     * @return Element
     */
    public Element createTuple(Element newElement) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO " + newElement.getTable() + " " + newElement.getColumnsNames() +" VALUES " +
                    newElement.getColumnsQuestionMark(),
                    Statement.RETURN_GENERATED_KEYS
            );

            Iterator<Object> col = newElement.iterator();
            int i = 1;
            while (col.hasNext()) {
                stmt.setObject(i, col.next());
                i++;
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating "+ newElement.getTable()  +" failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newElement.setPrimaryKeyValue(generatedKeys.getObject(1));
                } else {
                    throw new SQLException("Creating "+ newElement.getTable() +" failed, no ID obtained.");
                }
            }

            stmt.close();
            conn.close();

            return newElement;
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to create " + newElement.getTable(), se);
        }
    }

    public List<Element> createTuplesBatch(List<Element> elements) throws SQLException {
        if (elements == null || elements.isEmpty()) return Collections.emptyList();

        Element first = elements.get(0);

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " +
                first.getTable() + " " +
                first.getColumnsNames() + " VALUES " + first.getColumnsQuestionMark(), Statement.RETURN_GENERATED_KEYS)) {
            for (Element element : elements) {
                Iterator<Object> col = element.iterator();
                int i = 1;
                while (col.hasNext()) {
                    stmt.setObject(i++, col.next());
                }
                stmt.addBatch();
            }

            stmt.executeBatch();

            // Assign generated keys back to each element
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                int idx = 0;
                while (keys.next() && idx < elements.size()) {
                    elements.get(idx++).setPrimaryKeyValue(keys.getObject(1));
                }
            }

            return elements;

        } catch (SQLException e) {
            throw new RuntimeException("Batch insert failed for table: " + first.getTable(), e);
        }
    }

    public Map<String, Object> findByPrimaryKey(String table, String primaryKeyColumn, Object primaryKeyValue) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table + " WHERE " + primaryKeyColumn + " = ?")) {
            stmt.setObject(1, primaryKeyValue);
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                if (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.put(meta.getColumnName(i), rs.getObject(i));
                    }
                    return row;
                } else {
                    return null;
                }
            }
        }
    }


    /**
     * This method is to update tuples, mainly used for tuple file edit. Does not work for tables with composite keys.
     * @param element
     * @return Element
     */
    public Element updateTuple(Element element) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(element.getTable()).append(" SET ");

        String[] columnNames = element.getColumnNamesArray();
        Object[] columnValues = element.getColumnValues();

        for (int i = 0; i < columnNames.length; i++) {
            sql.append(columnNames[i]).append(" = ?");
            if (i < columnNames.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(" WHERE ").append(element.getPrimaryKeyColumn()).append(" = ?");

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int i = 1;
            for (Object value : columnValues) {
                stmt.setObject(i++, value);
            }

            stmt.setObject(i, element.getPrimaryKeyValue()); // WHERE condition

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating " + element.getTable() + " failed, no rows affected.");
            }

            return element;

        } catch (SQLException se) {
            System.out.println("SQL Exception: " + se.getMessage());
            throw new RuntimeException("Failed to update " + element.getTable(), se);
        }
    }

    /**
     * This method is to delete a tuple, mainly used for tuple deletion.
     * @param element
     */
    public boolean deleteTuple(Element element){
        try {
            if (!VALID_TABLES.contains(element.getTable())) {
                throw new IllegalArgumentException("Invalid table: " + element.getTable());
            }
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM " + element.getTable() + " WHERE " + element.getPrimaryKeyColumn() +" = ?"
            );

            stmt.setObject(1, element.getPrimaryKeyValue());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting "+ element +" failed, no rows affected.");
            }

            stmt.close();
            conn.close();
            return true;
        } catch (SQLException se) {
            System.out.println("SQL Exception:" + se.getMessage());
            throw new RuntimeException("Failed to delete "+ element, se);
        }
    }


}
