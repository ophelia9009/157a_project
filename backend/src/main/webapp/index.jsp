<%--
  index.jsp - Main landing page for the forum application
  Displays welcome message and provides navigation to login/signup pages
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome to Forum</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <%-- Navigation links section --%>
    <div class="nav-links">
        <a href="forum/home.jsp">Continue As Guest</a>
        <a href="auth/login.jsp">Login</a>
        <a href="auth/signup.jsp">Sign Up</a>
    </div>

    <%-- Main welcome content section --%>
    <div class="welcome">
        <h1>Welcome to Our Forum</h1>
        <p>Join the discussion with our community</p>
    </div>
</body>
</html>