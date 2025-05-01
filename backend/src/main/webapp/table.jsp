<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.dao.UserDAO, com.example.dao.BaseDAO, com.example.model.User, java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User List</title>
    <style>
        table {
            border-collapse: collapse;
            width: 80%;
            margin: 20px auto;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h1>User List</h1>
    <%
        UserDAO userDAO = new UserDAO();
        List<Map<String, Object>> users = userDAO.getTable("users", new String[]{"UserID", "Username", "Password", "Email", "RegisterDate"});
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Register Date</th>
        </tr>
        <% for(Map<String, Object> user : users) { %>
        <tr>
            <td><%= user.get("UserID") %></td>
            <td><%= user.get("Username") %></td>
            <td><%= user.get("Email") %></td>
            <td><%= user.get("RegisterDate") %></td>
        </tr>
        <% } %>
    </table>
    <h1>Subforum List</h1>
    <%
                BaseDAO baseDAO = new BaseDAO();
                List<Map<String, Object>> subforums = baseDAO.getTable("subforums", new String[]{"SubforumID", "Name", "Description", "CreationDate",
                                                                                                                 "SubscriberCount", "LastUpdated"});
            %>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Subscriber Count</th>
                </tr>
                <% for(Map<String, Object> subforum : subforums) { %>
                <tr>
                    <td><%= subforum.get("SubforumID") %></td>
                    <td><%= subforum.get("Name") %></td>
                    <td><%= subforum.get("Description") %></td>
                    <td><%= subforum.get("SubscriberCount") %></td>
                </tr>
                <% } %>
            </table>
        <h1>Selected Post List With Condition</h1>
                <%
                    List<Map<String, Object>> posts = baseDAO.getTableWithCondition("posts", new String[]{"PostID",
                    "Title", "BodyText", "CreationDate","Rating", "UserID", "SubforumID"}, "PostID < ?", new Object[]{10} );
                %>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>BodyText</th>
                        <th>Rating</th>
                        <th>Poster</th>
                    </tr>
                    <% for(Map<String, Object> post : posts) { %>
                    <tr>
                        <td><%= post.get("PostID") %></td>
                        <td><%= post.get("Title") %></td>
                        <td><%= post.get("BodyText") %></td>
                        <td><%= post.get("Rating") %></td>
                        <td><%= post.get("UserID") %></td>
                    </tr>
                    <% } %>
                </table>
</body>
</html>