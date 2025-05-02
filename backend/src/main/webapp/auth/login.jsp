<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.dao.UserDAO, edu.sjsu.cs157a.forum.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <h1>Login</h1>
    
    <%
    String error = null;
    
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || username.isEmpty() ||
            password == null || password.isEmpty()) {
            error = "Username and password are required";
        } else {
            try {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserByUsername(username);

                if (user == null || !password.equals(user.getPassword())) {
                    error = "Invalid username or password";
                } else {
                    session.setAttribute("user", user);
                    response.sendRedirect("../forum/home.jsp");
                    return;
                }
            } catch (Exception e) {
                error = "Login failed: " + e.getMessage();
            }
        }
    }
    %>
    
    <% if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>
    
    <form method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        
        <button type="submit">Login</button>
    </form>
    
    <p>Don't have an account? <a href="signup.jsp">Sign up here</a></p>
</body>
</html>