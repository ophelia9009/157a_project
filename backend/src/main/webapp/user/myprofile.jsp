<%--
  myprofile.jsp - User profile management page
  Displays profile information and handles account actions
--%>
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
    <%-- Check user session --%>
    <%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("../auth/login.jsp");
        return;
    }
    %>
    <%-- Profile display section --%>
    <h1>My Profile</h1>
    <div class="profile-info">
        <p>TODO:  add User Profile Display</p>
    </div>

    <%-- Profile action buttons --%>
    <button>Edit(TODO)</button>
    <button class="delete-btn" onclick="confirmDelete()">Delete My Account(TODO)</button>
    
    <%-- JavaScript functions --%>
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