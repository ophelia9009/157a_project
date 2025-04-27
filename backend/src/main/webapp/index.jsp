<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.dao.UserDAO, com.example.model.User, java.util.List" %>
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
        List<User> users = userDAO.getAllUsers();
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Register Date</th>
        </tr>
        <% for(User user : users) { %>
        <tr>
            <td><%= user.getUserID() %></td>
            <td><%= user.getUsername() %></td>
            <td><%= user.getEmail() %></td>
            <td><%= user.getRegisterDate() %></td>
        </tr>
        <% } %>
    </table>
</body>
</html>