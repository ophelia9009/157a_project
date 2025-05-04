<%--
  viewpost.jsp - Post viewing management page
  Displays post information and comments
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.sjsu.cs157a.forum.model.Post" %>
<%@ page import="edu.sjsu.cs157a.forum.dao.PostDAO" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/forum/home.jsp">View Forum</a>
        <a href="${pageContext.request.contextPath}/forum/subforumview.jsp">View Subforum</a>
        <a href="${pageContext.request.contextPath}/index.jsp">Logout</a>
    </div>
    <%-- Check user session --%>
    <%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
        return;
    }
    %>
    <%-- Get Post session --%>
    <%
    String postIdParam = request.getParameter("postId");
    if (postIdParam == null) {
        response.sendRedirect(request.getContextPath() + "/forum/home.jsp");
        return;
    }

    int postId = Integer.parseInt(postIdParam);
    PostDAO postDAO = new PostDAO();
    Post post = postDAO.getPostByID(postId);
    if (post == null) {
        out.println("<p>Post not found.</p>");
        return;
    }
    %>
    <%-- Post display section --%>
    <h1>My Post</h1>
    <div class="post-info">
        <p><strong>Post ID:</strong> <%= post.getId() %></p>
        <p><strong>Title:</strong> <%= post.getTitle() %></p>
        <p><strong>Body Text:</strong> <%= post.getBodyText() %></p>
        <p><strong>Creation Date:</strong> <%= post.getCreationDate() %></p>
        <p><strong>Last Updated:</strong> <%= post.getLastUpdated() %></p>
    </div>

    <%-- Post action form --%>
    <form id="postForm" method="post" action="${pageContext.request.contextPath}/api/postAction">
        <input type="hidden" name="postId" value="<%= post.getId() %>">
        <button type="submit" name="action" value="edit">Edit Post</button>
        <button type="submit" name="action" value="delete" class="delete-btn" onclick="return confirmDelete()">Delete My Post</button>
    </form>
    <script>
        function confirmDelete() {
            return confirm("Are you sure you want to delete this post?");
        }
    </script>

</body>
</html>