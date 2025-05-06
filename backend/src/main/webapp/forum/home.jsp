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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
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


    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Handle Create Subforum Modal with Bootstrap
        const modal = new bootstrap.Modal(document.getElementById('subforumModal'));
        const btn = document.getElementById('createSubforumBtn');

        btn.onclick = function() {
            modal.show();
        }

        // Handle Subforum Creation
        document.getElementById('subforumForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const name = document.getElementById('subforumName').value;
            const description = document.getElementById('subforumDesc').value;

            fetch('/backend/api/subforums', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: name,
                    description: description
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to create subforum');
                }
                return response.json();
            })
            .then(data => {
                // Show success message with Bootstrap toast
                const toastContainer = document.createElement('div');
                toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
                toastContainer.style.zIndex = '11';
                toastContainer.innerHTML = `
                    <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Subforum created successfully!
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `;
                document.body.appendChild(toastContainer);
                const toastEl = toastContainer.querySelector('.toast');
                const toast = new bootstrap.Toast(toastEl);
                toast.show();

                modal.hide();
                setTimeout(() => window.location.reload(), 1500);
            })
            .catch(error => {
                alert('Error: ' + error.message);
            });
        });

        document.getElementById('filterForm').addEventListener('submit', function (e) {
            e.preventDefault(); // Prevent default form submission

            const formData = new FormData(this);
            const query = new URLSearchParams();

            for (const [key, value] of formData.entries()) {
                if (value) query.append(key, value);
            }

            fetch('backend/api/subforumsearch?' + query.toString())
                .then(res => {
                    if (!res.ok) throw new Error("Failed to fetch subforums");
                    return res.json();
                })
                .then(data => {
                    const resultsDiv = document.getElementById('results');
                    resultsDiv.innerHTML = '';

                    if (data.length === 0) {
                        resultsDiv.innerHTML = '<div class="alert alert-info">No results found.</div>';
                        return;
                    }

                    const row = document.createElement('div');
                    row.className = 'row';

                    data.forEach(subforum => {
                        const col = document.createElement('div');
                        col.className = 'col-md-4 mb-3';
                        col.innerHTML = `
                            <div class="card h-100">
                                <div class="card-header">
                                    <h5 class="card-title">${subforum.name}</h5>
                                </div>
                                <div class="card-body">
                                    <p class="card-text">${subforum.description}</p>
                                </div>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item">Subscribers: ${subforum.subscriberCount}</li>
                                    <li class="list-group-item">Created: ${subforum.creationDate}</li>
                                    <li class="list-group-item">Updated: ${subforum.lastUpdated}</li>
                                </ul>
                            </div>
                        `;
                        row.appendChild(col);
                    });

                    resultsDiv.appendChild(row);
                })
                .catch(err => {
                    document.getElementById('results').innerHTML = `<div class="alert alert-danger">${err.message}</div>`;
                });
        });
    </script>
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
