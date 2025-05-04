<%--
  viewpost.jsp - Post viewing management page
  Displays post information and comments
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.sjsu.cs157a.forum.model.Post" %>
<%@ page import="edu.sjsu.cs157a.forum.dao.PostDAO" %>
<%@ page import="edu.sjsu.cs157a.forum.model.Comment" %>
<%@ page import="edu.sjsu.cs157a.forum.dao.CommentDAO" %>
<%@ page import="edu.sjsu.cs157a.forum.dao.UserDAO" %>
<%@ page import="java.sql.SQLException" %>


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
    <div class="post-header">
        <h1><%= post.getTitle() %></h1>
        <div class="post-meta">
            <span class="post-id">#<%= post.getId() %></span>
            <span class="post-date">
                Posted: <%= new java.text.SimpleDateFormat("MMM dd, yyyy").format(post.getCreationDate()) %>
            </span>
        </div>
    </div>
    <div class="post-content">
        <div class="post-body">
            <%= post.getBodyText() %>
        </div>
        <div class="post-footer">
            Last updated: <%= new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a").format(post.getLastUpdated()) %>
        </div>
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
        
        function confirmCommentDelete() {
            return confirm("Are you sure you want to delete this comment?");
        }
    </script>

    <%-- Comments display section --%>
    <div class="comments-section">
        <h2>Comments</h2>
        <div class="comments-container">
        <%
        CommentDAO commentDAO = new CommentDAO();
        UserDAO userDAO = new UserDAO();
        try {
            List<Comment> comments = commentDAO.getCommentsByPost(postId);
            if (comments.isEmpty()) {
                out.println("<p class='no-comments'>No comments yet. Be the first to comment!</p>");
            } else {
                for (Comment comment : comments) {
        %>
            <div class="comment-card">
                <div class="comment-content">
                    <div class="comment-header">
                        <span class="comment-author"><%= userDAO.getUserById(comment.getUserID()).getUsername() %></span>
                        <span class="comment-date">
                            <%= new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a").format(comment.getCreationDate()) %>
                        </span>
                        <% if (comment.getUserID() == user.getUserID()) { %>
                            <form class="comment-delete-form" method="post" action="${pageContext.request.contextPath}/api/commentAction">
                                <input type="hidden" name="commentId" value="<%= comment.getCommentID() %>">
                                <input type="hidden" name="postId" value="<%= postId %>">
                                <button type="submit" name="action" value="delete" class="delete-comment-btn" onclick="return confirmCommentDelete()">
                                    <i class="delete-icon">🗑️</i> Delete
                                </button>
                            </form>
                        <% } %>
                    </div>
                    <div class="comment-body">
                        <%= comment.getCommentText() %>
                    </div>
                    <div class="comment-footer">
                        <span class="comment-rating">
                            <i class="rating-icon">★</i> <%= comment.getRating() %>
                        </span>
                        <span class="comment-updated">
                            Updated: <%= new java.text.SimpleDateFormat("MMM dd, yyyy").format(comment.getLastUpdated()) %>
                        </span>
                    </div>
                </div>
            </div>
        <%
                }
            }
        } catch (SQLException e) {
            out.println("<p class='error-message'>Error loading comments: " + e.getMessage() + "</p>");
        }
        %>
        </div>
    </div>
</body>
</html>