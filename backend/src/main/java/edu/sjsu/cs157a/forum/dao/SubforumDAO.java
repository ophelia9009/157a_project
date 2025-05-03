package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Subforum;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubforumDAO extends BaseDAO {
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
                        rs.getString("SubforumID"),
                        rs.getString("Name"),
                        rs.getTimestamp("CreationDate"),
                        rs.getString("Description"),
                        rs.getString("SubscriberCount"),
                        rs.getTimestamp("LastUpdated")
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
