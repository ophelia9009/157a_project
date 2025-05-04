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
        
        // Function to open edit comment modal
        function openEditModal(commentId, commentText) {
            const editModal = document.getElementById('editCommentModal');
            const editCommentId = document.getElementById('editCommentId');
            const editCommentText = document.getElementById('editCommentText');
            
            editCommentId.value = commentId;
            editCommentText.value = commentText;
            editModal.style.display = 'block';
        }
        
        // Wait for DOM to be fully loaded
        document.addEventListener('DOMContentLoaded', function() {
            // Handle Create Comment Modal
            const commentModal = document.getElementById('commentModal');
            const commentBtn = document.getElementById('createCommentBtn');
            const closeCommentSpan = document.getElementById('closeCommentModal');
            
            // Handle Edit Comment Modal
            const editCommentModal = document.getElementById('editCommentModal');
            const closeEditCommentSpan = document.getElementById('closeEditCommentModal');
            
            if (commentBtn) {
                commentBtn.onclick = function() {
                    commentModal.style.display = 'block';
                }
            }
            
            if (closeCommentSpan) {
                closeCommentSpan.onclick = function() {
                    commentModal.style.display = 'none';
                }
            }
            
            if (closeEditCommentSpan) {
                closeEditCommentSpan.onclick = function() {
                    editCommentModal.style.display = 'none';
                }
            }
            
            window.onclick = function(event) {
                if (event.target == commentModal) {
                    commentModal.style.display = 'none';
                }
                if (event.target == editCommentModal) {
                    editCommentModal.style.display = 'none';
                }
            }
            
            // Handle Comment Creation
            const commentForm = document.getElementById('commentForm');
            if (commentForm) {
                commentForm.addEventListener('submit', function(e) {
                    e.preventDefault();
                    
                    const postId = <%= post.getId() %>;
                    const commentText = document.getElementById('commentText').value;
                    
                    fetch('${pageContext.request.contextPath}/api/commentAction', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            postId: postId,
                            commentText: commentText
                        })
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Failed to create comment');
                        }
                        return response.json();
                    })
                    .then(data => {
                        alert('Comment added successfully!');
                        commentModal.style.display = 'none';
                        window.location.reload();
                    })
                    .catch(error => {
                        alert('Error: ' + error.message);
                    });
                });
            }
        });
    </script>

    <%-- Add Comment Button --%>
    <button id="createCommentBtn" style="margin: 20px 0;">Add Comment</button>

    <%-- Create Comment Modal --%>
    <div id="commentModal" style="display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.4);">
        <div style="background-color: #fefefe; margin: 15% auto; padding: 20px; border: 1px solid #888; width: 50%;">
            <span id="closeCommentModal" style="float: right; cursor: pointer;">&times;</span>
            <h2>Add New Comment</h2>
            <form id="commentForm">
                <input type="hidden" name="postId" value="<%= post.getId() %>">
                <label for="commentText">Comment:</label>
                <textarea id="commentText" name="commentText" required style="width: 100%; min-height: 100px;"></textarea><br><br>
                <button type="submit">Submit</button>
            </form>
        </div>
    </div>

    <%-- Edit Comment Modal --%>
    <div id="editCommentModal" style="display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.4);">
        <div style="background-color: #fefefe; margin: 15% auto; padding: 20px; border: 1px solid #888; width: 50%;">
            <span id="closeEditCommentModal" style="float: right; cursor: pointer;">&times;</span>
            <h2>Edit Comment</h2>
            <form id="editCommentForm" method="post" action="${pageContext.request.contextPath}/api/commentAction">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="commentId" id="editCommentId">
                <input type="hidden" name="postId" value="<%= post.getId() %>">
                <label for="editCommentText">Comment:</label>
                <textarea id="editCommentText" name="commentText" required style="width: 100%; min-height: 100px;"></textarea><br><br>
                <button type="submit">Save Changes</button>
            </form>
        </div>
    </div>

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
                            <div class="comment-actions">
                                <button class="edit-comment-btn" onclick="openEditModal(<%= comment.getCommentID() %>, '<%= comment.getCommentText().replace("'", "\\'").replace("\n", "\\n") %>')">
                                    <i class="edit-icon">✏️</i> Edit
                                </button>
                                <form class="comment-delete-form" method="post" action="${pageContext.request.contextPath}/api/commentAction">
                                    <input type="hidden" name="commentId" value="<%= comment.getCommentID() %>">
                                    <input type="hidden" name="postId" value="<%= postId %>">
                                    <button type="submit" name="action" value="delete" class="delete-comment-btn" onclick="return confirmCommentDelete()">
                                        <i class="delete-icon">🗑️</i> Delete
                                    </button>
                                </form>
                            </div>
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
