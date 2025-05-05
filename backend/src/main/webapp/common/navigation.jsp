<%-- 
  navigation.jsp - Simple reusable navigation component
  Include this file in other JSP pages with the appropriate parameters
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>

<%
// Get the current page parameter
String currentPage = request.getParameter("currentPage");
if (currentPage == null) {
    currentPage = "default";
}

// Check if user is logged in
User user = (User) session.getAttribute("user");
boolean isLoggedIn = (user != null);
%>

<!-- Navigation bar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">Forum</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <%-- Common link for all pages --%>
                <li class="nav-item">
                    <a class="nav-link <%= currentPage.equals("home") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/forum/home.jsp">
                        <i class="bi bi-house-fill"></i> Home
                    </a>
                </li>
                
                <%-- Page-specific links --%>
                
                <%-- For viewpost.jsp - Back to Subforum link --%>
                <% if (currentPage.equals("viewpost") && request.getParameter("subforumId") != null) { %>
                    <li class="nav-item">
                        <a class="nav-link" 
                           href="${pageContext.request.contextPath}/forum/subforumview.jsp?subforumId=<%= request.getParameter("subforumId") %>">
                            <i class="bi bi-arrow-left"></i> Back to Subforum
                        </a>
                    </li>
                <% } %>
                
                <%-- For subforumview.jsp - Create Post button --%>
                <% if (currentPage.equals("subforumview") && isLoggedIn && request.getParameter("subforumId") != null) { %>
                    <li class="nav-item">
                        <a class="nav-link" 
                           href="${pageContext.request.contextPath}/forum/newpost.jsp?subforumId=<%= request.getParameter("subforumId") %>">
                            <i class="bi bi-plus-circle"></i> Create Post
                        </a>
                    </li>
                <% } %>
                
                <%-- For editpost.jsp - Cancel Edit link --%>
                <% if (currentPage.equals("editpost") && request.getParameter("postId") != null) { %>
                    <li class="nav-item">
                        <a class="nav-link" 
                           href="${pageContext.request.contextPath}/forum/viewpost.jsp?postId=<%= request.getParameter("postId") %>">
                            <i class="bi bi-x-circle"></i> Cancel Edit
                        </a>
                    </li>
                <% } %>
                
                <%-- For newpost.jsp - Cancel New Post link --%>
                <% if (currentPage.equals("newpost") && request.getParameter("subforumId") != null) { %>
                    <li class="nav-item">
                        <a class="nav-link" 
                           href="${pageContext.request.contextPath}/forum/subforumview.jsp?subforumId=<%= request.getParameter("subforumId") %>">
                            <i class="bi bi-x-circle"></i> Cancel
                        </a>
                    </li>
                <% } %>
                
                <%-- For editprofile.jsp - Cancel Edit link --%>
                <% if (currentPage.equals("editprofile")) { %>
                    <li class="nav-item">
                        <a class="nav-link" 
                           href="${pageContext.request.contextPath}/user/myprofile.jsp">
                            <i class="bi bi-x-circle"></i> Cancel Edit
                        </a>
                    </li>
                <% } %>
                
                <%-- Authentication-based links --%>
                <% if (isLoggedIn) { %>
                    <li class="nav-item">
                        <a class="nav-link <%= currentPage.equals("profile") ? "active" : "" %>"
                           href="${pageContext.request.contextPath}/user/myprofile.jsp">
                            <i class="bi bi-person-circle"></i> My Profile
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                            <i class="bi bi-box-arrow-right"></i> Logout
                        </a>
                    </li>
                <% } else if (!request.getRequestURI().endsWith("/index.jsp")) { %>
                    <li class="nav-item">
                        <a class="nav-link <%= currentPage.equals("login") ? "active" : "" %>"
                           href="${pageContext.request.contextPath}/auth/login.jsp">
                            <i class="bi bi-box-arrow-in-right"></i> Login
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= currentPage.equals("signup") ? "active" : "" %>"
                           href="${pageContext.request.contextPath}/auth/signup.jsp">
                            <i class="bi bi-person-plus"></i> Sign Up
                        </a>
                    </li>
                <% } %>
                
                <%-- Continue as guest link for index page --%>
                <% if (!isLoggedIn && request.getRequestURI().endsWith("/index.jsp")) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/forum/home.jsp">
                            <i class="bi bi-person"></i> Continue As Guest
                        </a>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>