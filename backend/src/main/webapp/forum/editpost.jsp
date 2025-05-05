<%-- editpost.jsp - Post editing page --%>
<%@ page import="edu.sjsu.cs157a.forum.model.Post" %>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
    return;
}
Post post = (Post) request.getAttribute("post");
if (post == null) {
    response.sendRedirect(request.getContextPath() + "/forum/subforumview.jsp?subforumId=" + post.getSubforumID());
    return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Post</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/flatly/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h3 class="mb-0">Edit Post</h3>
                    </div>
                    <div class="card-body p-4">
                        <form method="post" action="${pageContext.request.contextPath}/api/postAction">
                            <input type="hidden" name="action" value="editSubmit">
                            <input type="hidden" name="postId" value="<%= post.getPostID() %>">
                            <input type="hidden" name="subforumId" value="<%=  post.getSubforumID() %>">
                            <div class="mb-3">
                                <label for="title" class="form-label">Title</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-card-heading"></i></span>
                                    <input type="text" class="form-control" id="title" name="title"
                                        value="<%= post.getTitle() %>" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="bodyText" class="form-label">Content</label>
                                <textarea class="form-control" id="bodyText" name="bodyText"
                                    rows="5" required><%= post.getBodyText() %></textarea>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save-fill me-2"></i>Save Changes
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
