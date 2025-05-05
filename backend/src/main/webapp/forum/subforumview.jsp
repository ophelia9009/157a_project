<%--
  subforumview.jsp - view a subforum
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import required DAOs and models --%>
<%@ page import="edu.sjsu.cs157a.forum.dao.UserDAO, edu.sjsu.cs157a.forum.model.User" %>
<%
    String subforumId = request.getParameter("subforumId");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Subforum View</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/flatly/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

    <script>
        function formatDate(dateString) {
            if (!dateString) return 'N/A';
            const date = new Date(dateString);
            return date.toLocaleString('en-US', {
                month: 'short',
                day: 'numeric',
                year: 'numeric',
                hour: 'numeric',
                minute: 'numeric'
            });
        }

        async function fetchPosts(subforumID) {
            console.log("Subforum ID is:", subforumID);
            try {
                const response = await fetch("/backend/api/subforums/" + subforumID + "/posts");
                if (!response.ok) {
                    console.log("Couldn't get posts");
                    return;
                }
                const posts = await response.json();
                console.log("Posts fetched:", posts);
                renderPosts(posts);
            } catch (error) {
                console.error("Error fetching posts:", error);
            }
        }

        function renderPosts(posts) {
            const container = document.getElementById("postsContainer");
            container.innerHTML = "";

            if (posts.length === 0) {
                container.innerHTML = '<div class="alert alert-info">No posts available.</div>';
                return;
            }
            
            const table = document.createElement('table');
            table.className = 'table table-striped table-hover w-100';
            table.style.tableLayout = 'fixed';
            table.innerHTML = `
                <thead class="table-primary">
                    <tr>
                        <th scope="col">Post ID</th>
                        <th scope="col">Title</th>
                        <th scope="col">Body</th>
                        <th scope="col">Creation Date</th>
                        <th scope="col">Rating</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            `;

            const tbody = table.querySelector('tbody');
            for (let i = 0; i < posts.length; i++) {
                const post = posts[i];
                const row = document.createElement('tr');
                const title = post.title || 'Untitled Post';
                const bodyText = post.bodytext || 'No content';
                const rating = post.rating || 0;
                const ratingClass = rating > 0 ? 'bg-success' : rating < 0 ? 'bg-danger' : 'bg-secondary';

                const formattedDate = formatDate(post.creationDate);
                
                row.innerHTML = `
                    <td>\${post.postID}</td>
                    <td>
                        <a href='viewpost.jsp?postId=\${post.postID}' class="text-decoration-none fw-bold">
                            \${title}
                        </a>
                    </td>
                    <td class="text-truncate" style="max-width: 200px;">
                        \${bodyText}
                    </td>
                    <td>\${formattedDate}</td>
                    <td>
                        <span class="badge \${ratingClass}">
                            \${rating}
                        </span>
                    </td>
                `;
                tbody.appendChild(row);
            }

            container.appendChild(table);
        }

        async function checkSubscriptionStatus(subforumId) {
            try {
                if (!subforumId || subforumId.trim() === '') {
                    console.error("Invalid subforumId");
                    return false;
                }
                console.log("Checking subscription status using url:" + `/backend/subscription?subforumId=` + subforumId);
                const encodedSubforumId = encodeURIComponent(subforumId);
                const response = await fetch(`/backend/subscription?subforumId=` + subforumId, {
                    credentials: 'include'
                });
                if (!response.ok) {
                    console.error("Failed to check subscription status");
                    return false;
                }
                const result = await response.json();
                return result.isSubscribed;
            } catch (error) {
                console.error("Error checking subscription status:", error);
                return false;
            }
        }

        async function updateSubscriptionButton(subforumId) {
            const isSubscribed = await checkSubscriptionStatus(subforumId);
            const btn = document.getElementById("subscriptionBtn");
            const text = document.getElementById("subscriptionText");
            
            if (isSubscribed) {
                btn.classList.remove("btn-primary");
                btn.classList.add("btn-danger");
                text.textContent = "Unsubscribe";
            } else {
                btn.classList.remove("btn-danger");
                btn.classList.add("btn-primary");
                text.textContent = "Subscribe";
            }
        }

        async function handleSubscription() {
            const subforumId = "<%= subforumId %>";
            const isSubscribed = await checkSubscriptionStatus(subforumId);
            const action = isSubscribed ? "unsubscribe" : "subscribe";
            
            try {
                const params = new URLSearchParams();
                params.append('action', action);
                params.append('subforumId', subforumId);
                
                const response = await fetch("/backend/subscription", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body: params
                });
                
                if (response.ok) {
                    const result = await response.json();
                    if (result.success) {
                        // Update button immediately based on action
                        const btn = document.getElementById("subscriptionBtn");
                        const text = document.getElementById("subscriptionText");
                        
                        if (action === "subscribe") {
                            btn.classList.remove("btn-primary");
                            btn.classList.add("btn-danger");
                            text.textContent = "Unsubscribe";
                        } else {
                            btn.classList.remove("btn-danger");
                            btn.classList.add("btn-primary");
                            text.textContent = "Subscribe";
                        }
                    } else {
                        alert("Subscription action failed");
                    }
                } else {
                    const error = await response.text();
                    alert(`Subscription error: ${error}`);
                }
            } catch (error) {
                console.error("Error:", error);
                alert("Error processing subscription");
            }
        }

        window.addEventListener("DOMContentLoaded", function () {
            fetchPosts("<%= subforumId %>");
            updateSubscriptionButton("<%= subforumId %>");
        });
    </script>
</head>
<body class="bg-light">
    <!-- Include navigation -->
    <jsp:include page="../common/navigation.jsp">
        <jsp:param name="currentPage" value="subforum" />
        <jsp:param name="subforumId" value="<%= subforumId %>" />
    </jsp:include>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="mb-1">Subforum View</h2>
                <p class="text-muted">Viewing posts for subforum ID: <%= subforumId %></p>
            </div>
            <div class="btn-group" role="group">
                <a href="newpost.jsp?subforumId=<%= subforumId %>" class="btn btn-success">
                    <i class="bi bi-plus-circle"></i> Create New Post
                </a>
                <button id="subscriptionBtn" class="btn btn-primary" onclick="handleSubscription()">
                    <i class="bi bi-bell"></i> <span id="subscriptionText">Subscribe</span>
                </button>
            </div>
        </div>

        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0">Posts</h4>
            </div>
            <div class="card-body">
                <div id="postsContainer" class="table-responsive">
                    <!-- Posts will be loaded here -->
                    <div class="d-flex justify-content-center">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
