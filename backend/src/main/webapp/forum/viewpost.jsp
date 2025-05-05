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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>View Post</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/flatly/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <%-- Check user session --%>
    <%
    User user = (User) session.getAttribute("user");
    boolean isGuest = (user == null);
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
        out.println("<div class='alert alert-danger'>Post not found.</div>");
        return;
    }
    %>

    <!-- Include navigation -->
    <jsp:include page="../common/navigation.jsp">
        <jsp:param name="currentPage" value="viewpost" />
        <jsp:param name="subforumId" value="<%= post.getSubforumID() %>" />
    </jsp:include>

    <div class="container mt-4">
        
        <%-- Post display section --%>
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-primary text-white">
                <h1 class="h3 mb-0"><%= post.getTitle() %></h1>
            </div>
            <div class="card-body">
                <div class="mb-4">
                    <div class="post-meta text-muted mb-3">
                        <small>
                            <span class="me-3"><i class="bi bi-hash"></i> <%= post.getPostID() %></span>
                            <span><i class="bi bi-calendar-event"></i> Posted: <%= new java.text.SimpleDateFormat("MMM dd, yyyy").format(post.getCreationDate()) %></span>
                        </small>
                    </div>
                    <div class="post-body">
                        <p><%= post.getBodyText() %></p>
                    </div>
                    <div class="post-footer text-muted mt-3">
                        <small><i class="bi bi-clock"></i> Last updated: <%= new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a").format(post.getLastUpdated()) %></small>
                    </div>
                </div>

                <%-- Post action form --%>
                <% if (!isGuest && user.getUserID().equals(post.getUserID())) { %>
                <div class="d-flex justify-content-end">
                    <form id="postForm" method="post" action="${pageContext.request.contextPath}/api/postAction" class="d-flex gap-2">
                        <input type="hidden" name="postId" value="<%= post.getPostID() %>">
                        <input type="hidden" name="subforumId" value="<%= post.getSubforumID() %>">
                        <button type="submit" name="action" value="edit" class="btn btn-outline-primary">
                            <i class="bi bi-pencil-square"></i> Edit Post
                        </button>
                        <button type="submit" name="action" value="delete" class="btn btn-outline-danger" onclick="return confirmDelete()">
                            <i class="bi bi-trash"></i> Delete Post
                        </button>
                    </form>
                </div>
                <% } %>
            </div>
        </div>
        <% if (!isGuest && user.getUserID().equals(post.getUserID())) { %>
            <!-- Edit/Delete Post Form -->
        <!-- Add Comment Button and Modal -->
        <% if (!isGuest) { %>
        <button id="createCommentBtn" class="btn btn-primary mb-4">
            <i class="bi bi-chat-left-text"></i> Add Comment
        </button>
        <% } %>
        <!-- Create Comment Modal -->
        <div class="modal fade" id="commentModal" tabindex="-1" aria-labelledby="commentModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="commentModalLabel">Add New Comment</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="commentForm">
                            <input type="hidden" id="commentPostId" name="postId" value="<%= post.getPostID() %>">
                            <div class="mb-3">
                                <label for="commentText" class="form-label">Comment:</label>
                                <textarea id="commentText" name="commentText" class="form-control" rows="5" required></textarea>
                            </div>
                            <div class="text-end">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Comment Modal -->
        <div class="modal fade" id="editCommentModal" tabindex="-1" aria-labelledby="editCommentModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editCommentModalLabel">Edit Comment</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editCommentForm" method="post" action="${pageContext.request.contextPath}/api/commentAction">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="commentId" id="editCommentId">
                            <input type="hidden" name="postId" value="<%= post.getPostID() %>">
                            <div class="mb-3">
                                <label for="editCommentText" class="form-label">Comment:</label>
                                <textarea id="editCommentText" name="commentText" class="form-control" rows="5" required></textarea>
                            </div>
                            <div class="text-end">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Save Changes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <% } %>

        <%-- Comments display section --%>
        <div class="card shadow-sm mb-5">
            <div class="card-header bg-light">
                <h2 class="h4 mb-0">Comments</h2>
            </div>
            <div class="card-body">
                <div class="comments-container">
                <%
                CommentDAO commentDAO = new CommentDAO();
                UserDAO userDAO = new UserDAO();
                try {
                    List<Comment> comments = commentDAO.getCommentsByPost(postId);
                    if (comments.isEmpty()) {
                        out.println("<div class='alert alert-info'>No comments yet. Be the first to comment!</div>");
                    } else {
                        for (Comment comment : comments) {
                %>
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <div>
                                    <h6 class="mb-0 fw-bold"><%= userDAO.getUserById(comment.getUserID()).getUsername() %></h6>
                                    <small class="text-muted">
                                        <i class="bi bi-clock"></i> <%= new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a").format(comment.getCreationDate()) %>
                                    </small>
                                </div>
                                <% if (comment.getUserID().equals(user.getUserID())) { %>
                                    <div class="d-flex">
                                        <button class="btn btn-sm btn-outline-primary me-2"
                                                data-comment-id="<%= comment.getCommentID() %>"
                                                data-comment-text="<%= comment.getCommentText().replace("\"", "&quot;") %>"
                                                onclick="openEditModal(this)">
                                            <i class="bi bi-pencil"></i> Edit
                                        </button>
                                        <form class="d-inline" method="post" action="${pageContext.request.contextPath}/api/commentAction">
                                            <input type="hidden" name="commentId" value="<%= comment.getCommentID() %>">
                                            <input type="hidden" name="postId" value="<%= postId %>">
                                            <button type="submit" name="action" value="delete" class="btn btn-sm btn-outline-danger" 
                                                    onclick="return confirmCommentDelete()">
                                                <i class="bi bi-trash"></i> Delete
                                            </button>
                                        </form>
                                    </div>
                                <% } %>
                            </div>
                            <p class="card-text"><%= comment.getCommentText() %></p>
                            <div class="d-flex justify-content-between align-items-center mt-2">
                                <span class="badge bg-secondary">
                                    <i class="bi bi-star-fill"></i> <%= comment.getRating() %>
                                </span>
                                <small class="text-muted">
                                    Updated: <%= new java.text.SimpleDateFormat("MMM dd, yyyy").format(comment.getLastUpdated()) %>
                                </small>
                            </div>
                        </div>
                    </div>
                <%
                        }
                    }
                } catch (SQLException e) {
                    out.println("<div class='alert alert-danger'>Error loading comments: " + e.getMessage() + "</div>");
                }
                %>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function confirmDelete() {
            return confirm("Are you sure you want to delete this post?");
        }
        
        function confirmCommentDelete() {
            return confirm("Are you sure you want to delete this comment?");
        }
        
        // Function to open edit comment modal
        function openEditModal(button) {
            const commentId = button.getAttribute('data-comment-id');
            const commentText = button.getAttribute('data-comment-text');
            const editModal = new bootstrap.Modal(document.getElementById('editCommentModal'));
            document.getElementById('editCommentId').value = commentId;
            document.getElementById('editCommentText').value = commentText;
            editModal.show();
        }
        
        // Wait for DOM to be fully loaded
        document.addEventListener('DOMContentLoaded', function() {
            // Handle Create Comment Modal with Bootstrap
            const commentModal = new bootstrap.Modal(document.getElementById('commentModal'));
            const commentBtn = document.getElementById('createCommentBtn');
            
            if (commentBtn) {
                commentBtn.onclick = function() {
                    commentModal.show();
                }
            }
            
            // Handle Comment Creation
            const commentForm = document.getElementById('commentForm');
            if (commentForm) {
                commentForm.addEventListener('submit', function(e) {
                    e.preventDefault();
                    
                    const postId = document.getElementById('commentPostId').value;
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
                        // Show success toast
                        const toastContainer = document.createElement('div');
                        toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
                        toastContainer.style.zIndex = '11';
                        toastContainer.innerHTML = `
                            <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                                <div class="d-flex">
                                    <div class="toast-body">
                                        Comment added successfully!
                                    </div>
                                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                                </div>
                            </div>
                        `;
                        document.body.appendChild(toastContainer);
                        const toastEl = toastContainer.querySelector('.toast');
                        const toast = new bootstrap.Toast(toastEl);
                        toast.show();
                        
                        commentModal.hide();
                        setTimeout(() => window.location.reload(), 1500);
                    })
                    .catch(error => {
                        // Show error toast
                        const toastContainer = document.createElement('div');
                        toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
                        toastContainer.style.zIndex = '11';
                        toastContainer.innerHTML = `
                            <div class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
                                <div class="d-flex">
                                    <div class="toast-body">
                                        Error: ${error.message}
                                    </div>
                                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                                </div>
                            </div>
                        `;
                        document.body.appendChild(toastContainer);
                        const toastEl = toastContainer.querySelector('.toast');
                        const toast = new bootstrap.Toast(toastEl);
                        toast.show();
                    });
                });
            }
        });
    </script>
</body>
</html>
