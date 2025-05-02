<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <div class="nav-links">
        <a href="../forum/home.jsp">View Forum</a>
        <a href="../index.jsp">Logout</a>
    </div>
    <%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("../auth/login.jsp");
        return;
    }
    %>
    <h1>My Profile</h1>
    <div class="profile-info">
        <p>TODO:  add User Profile Display</p>
    </div>
    <button>Edit</button>
    <button class="delete-btn" onclick="confirmDelete()">Delete My Account</button>
    
    <script>
    function confirmDelete() {
        if(confirm('Are you sure you want to delete your account? This cannot be undone.')) {
            // TODO: Add account deletion logic
            window.location.href = '../auth/deleteAccount';
        }
    }
    </script>
</body>
</html>