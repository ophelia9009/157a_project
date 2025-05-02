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

    <%-- Profile action form --%>
    <form id="profileForm" method="post" action="../auth/profileAction">
        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
        <button type="submit" name="action" value="edit">Edit Profile</button>
        <button type="submit" name="action" value="delete" class="delete-btn" onclick="return confirmDelete()">Delete My Account</button>
    </form>
    
    <%-- JavaScript functions --%>
    <script>
    function confirmDelete() {
        return confirm('Are you sure you want to delete your account? This cannot be undone.');
    }
    </script>
</body>
</html>