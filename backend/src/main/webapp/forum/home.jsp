<%--
  home.jsp - Main forum page after login
  Displays navigation and subforum listing
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.dao.SubforumDAO" %>
<%@ page import="edu.sjsu.cs157a.forum.model.Subforum" %>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Forum Home</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/flatly/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <script src="/backend/js/home.js"></script>
</head>
<body class="bg-light">
    <!-- Include navigation -->
    <jsp:include page="../common/navigation.jsp">
        <jsp:param name="currentPage" value="home" />
    </jsp:include>

    <div class="container mt-4">
        <!-- Create Subforum Modal -->
        <div class="modal fade" id="subforumModal" tabindex="-1" aria-labelledby="subforumModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="subforumModalLabel">Create New Subforum</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="subforumForm">
                            <div class="mb-3">
                                <label for="subforumName" class="form-label">Name:</label>
                                <input type="text" class="form-control" id="subforumName" name="name" required>
                            </div>
                            <div class="mb-3">
                                <label for="subforumDesc" class="form-label">Description:</label>
                                <textarea class="form-control" id="subforumDesc" name="description" rows="3" required></textarea>
                            </div>
                            <div class="text-end">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Create</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Main content section -->
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h1 class="h3 mb-0">Forum Home</h1>
            </div>
            <div class="card-body">
                <p class="text-muted">Browse all subforums</p>
            </div>
        </div>


<div class="container mb-4">
    <div class="card">
        <div class="card-header bg-light">
            <h4>Search Subforums</h4>
        </div>
        <div class="card-body">
            <form id="filterForm" method="get" action="/backend/api/subforumsearch">
                <div class="mb-3">
                    <input type="text" name="filterName" placeholder="Search by Name" class="form-control">
                </div>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Creation Date Range</label>
                        <div class="input-group">
                            <input type="date" name="minCreationDate" class="form-control" placeholder="From">
                            <span class="input-group-text">to</span>
                            <input type="date" name="maxCreationDate" class="form-control" placeholder="To">
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Subscribers Range</label>
                        <div class="input-group">
                            <input type="number" name="minSubscribers" class="form-control" placeholder="Min">
                            <span class="input-group-text">to</span>
                            <input type="number" name="maxSubscribers" class="form-control" placeholder="Max">
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Last Updated Range</label>
                        <div class="input-group">
                            <input type="date" name="minLastUpdated" class="form-control" placeholder="From">
                            <span class="input-group-text">to</span>
                            <input type="date" name="maxLastUpdated" class="form-control" placeholder="To">
                        </div>
                    </div>
                </div>
                <div class="text-end">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Search
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <div id="results" class="mt-3">
        <!-- Search results will appear here -->
    </div>
</div>

        <!-- Create Subforum Button -->
        <button id="createSubforumBtn" class="btn btn-success mb-3">
            <i class="bi bi-plus-circle"></i> Create Subforum
        </button>


    <!-- Display all subforums by lastupdated -->
    <div class="container mb-5">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h2 class="h4 mb-0">All Subforums</h2>
            </div>
            <div class="card-body">
                <div class="list-group">
                    <%
                        SubforumDAO subforumDAO = new SubforumDAO();
                        List<Subforum> subforums = subforumDAO.getAllSubforumsOrderedByLastUpdated();

                        if (subforums.isEmpty()) {
                    %>
                        <div class="alert alert-info">No subforums found.</div>
                    <%
                        } else {
                            for (Subforum subforum : subforums) {
                    %>
                                <a href="subforumview.jsp?subforumId=<%= subforum.getSubforumID() %>" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                                    <div>
                                        <h5 class="mb-1"><%= subforum.getName() %></h5>
                                        <small class="text-muted">Last updated: <%= subforum.getLastUpdated() %></small>
                                    </div>
                                    <%
                                        User currentUser = (User) session.getAttribute("user");
                                        if (currentUser != null) {
                                            List<Subforum> subscribedSubforums = subforumDAO.getSubscribedSubforums(currentUser.getUserID());
                                            for (Subforum subscribed : subscribedSubforums) {
                                                if (subscribed.getSubforumID().equals(subforum.getSubforumID())) {
                                    %>
                                    <span class="badge bg-primary rounded-pill">
                                        <i class="bi bi-bell-fill"></i> Subscribed
                                    </span>
                                    <%
                                                    break;
                                                }
                                            }
                                        }
                                    %>
                                </a>
                    <%
                            }
                        }
                    %>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
