<%--
  newpost.jsp - Create a new post in a subforum
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.dao.SubforumDAO" %>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create New Post</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/flatly/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <!-- Navigation bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="#">Forum</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="home.jsp">
                            <i class="bi bi-house-fill"></i> Home
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <%
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
                return;
            }

            String subforumIdParam = request.getParameter("subforumId");
            int subforumId = -1;
            Map<String, Object> subforum = null;

            try {
                subforumId = Integer.parseInt(subforumIdParam);
                SubforumDAO dao = new SubforumDAO();
                subforum = dao.findByPrimaryKey("Subforums", "SubforumID", subforumId);
            } catch (Exception e) {
                out.println("<div class='alert alert-danger'><i class='bi bi-exclamation-triangle-fill me-2'></i>Invalid or missing subforum ID.</div>");
                return;
            }

            if (subforum == null) {
                out.println("<div class='alert alert-danger'><i class='bi bi-exclamation-triangle-fill me-2'></i>Subforum not found.</div>");
                return;
            }

            int userId = user.getUserID();
        %>

        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h2 class="h4 mb-0">Create a Post in: <%= subforum.get("Name") %></h2>
            </div>
            <div class="card-body">
                <!-- <form method="post" action="${pageContext.request.contextPath}/api/posts"> -->
                <form method="post" action="/backend/api/posts">
                    <input type="hidden" name="subforumId" value="<%= subforumId %>">
                    <input type="hidden" name="userId" value="<%= userId %>">

                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" id="title" name="title" required>
                    </div>

                    <div class="mb-3">
                        <label for="bodyText" class="form-label">Body</label>
                        <textarea class="form-control" id="bodyText" name="bodyText" rows="10" required></textarea>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="subforumview.jsp?subforumId=<%= subforumId %>" class="btn btn-secondary">
                            <i class="bi bi-arrow-left"></i> Cancel
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-send"></i> Create Post
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
