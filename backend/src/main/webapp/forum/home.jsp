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
    <title>Forum Home</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <%-- Navigation links section --%>
    <div class="nav-links">
        <a href="../user/myprofile.jsp">My Profile</a>
        <a href="../index.jsp">Logout</a>
        <%--Maybe this link can be moved elsewhere for only a single subforum page
          <a href="newpost.jsp?subforumId=<%= subforum.get("SubforumID") %>">Create Post</a>--%>

    </div>

    <%-- Create Subforum Button --%>
    <button id="createSubforumBtn" style="position: absolute; left: 20px; top: 20px;">Create Subforum</button>

    <%-- Create Subforum Modal --%>
    <div id="subforumModal" style="display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.4);">
        <div style="background-color: #fefefe; margin: 15% auto; padding: 20px; border: 1px solid #888; width: 50%;">
            <span id="closeModal" style="float: right; cursor: pointer;">&times;</span>
            <h2>Create New Subforum</h2>
            <form id="subforumForm">
                <label for="subforumName">Name:</label>
                <input type="text" id="subforumName" name="name" required><br><br>
                <label for="subforumDesc">Description:</label>
                <textarea id="subforumDesc" name="description" required></textarea><br><br>
                <button type="submit">Create</button>
            </form>
        </div>
    </div>

    <%-- Main content section --%>
    <h1>Sub Forum List</h1>


    <script>
        // Handle Create Subforum Modal
        const modal = document.getElementById('subforumModal');
        const btn = document.getElementById('createSubforumBtn');
        const span = document.getElementById('closeModal');

        btn.onclick = function() {
            modal.style.display = 'block';
        }

        span.onclick = function() {
            modal.style.display = 'none';
        }

        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
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
                alert('Subforum created successfully!');
                modal.style.display = 'none';
                window.location.reload();
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

            fetch('/api/subforumsearch?' + query.toString())
                .then(res => {
                    if (!res.ok) throw new Error("Failed to fetch subforums");
                    return res.json();
                })
                .then(data => {
                    const resultsDiv = document.getElementById('results');
                    resultsDiv.innerHTML = '';

                    if (data.length === 0) {
                        resultsDiv.innerHTML = '<p>No results found.</p>';
                        return;
                    }

                    data.forEach(subforum => {
                        const div = document.createElement('div');
                        div.className = 'subforum';
                        div.innerHTML = `
                            <h3>${subforum.name}</h3>
                            <p>${subforum.description}</p>
                            <p>Subscribers: ${subforum.subscriberCount}</p>
                            <p>Created: ${subforum.creationDate}</p>
                            <p>Updated: ${subforum.lastUpdated}</p>
                        `;
                        resultsDiv.appendChild(div);
                    });
                })
                .catch(err => {
                    document.getElementById('results').innerHTML = `<p style="color:red;">${err.message}</p>`;
                });
        });
    </script>

    <form method="get" action="/api/subforumsearch">
        <input type="text" name="filterName" placeholder="Search by Name">
        <div>
            <label>min, max creation date: </label>
            <input type="date" name="minCreationDate" placeholder="min creation date">
            <input type="date" name="maxCreationDate" placeholder="Max creation date">
        </div>
        <div>
                <input type="number" name="minSubscribers" placeholder="Min Subscribers">
                <input type="number" name="maxSubscribers" placeholder="Max Subscribers">
        </div>
        <div>
            <label> min, max last updated: </label>
            <input type="date" name="minLastUpdated" placeholder="min last Updated">
            <input type="date" name="maxLastUpdated" placeholder="max last End">
        </div>
        <button type="submit">Filter</button>
    </form>

    <%-- Display all subforums by lastupdated --%>
    <div id="allSubforums">
        <h2>All Subforums</h2>
        <div class="subforum-list">
            <%
                SubforumDAO subforumDAO = new SubforumDAO();
                List<Subforum> subforums = subforumDAO.getAllSubforumsOrderedByLastUpdated();

                if (subforums.isEmpty()) {
            %>
                <p>No subforums found.</p>
            <%
                } else {
                    for (Subforum subforum : subforums) {
            %>
                        <div class="subforum">
                            <h3>
                                <%
                                    User currentUser = (User) session.getAttribute("user");
                                    if (currentUser != null) {
                                        List<Subforum> subscribedSubforums = subforumDAO.getSubscribedSubforums(currentUser.getUserID());
                                        for (Subforum subscribed : subscribedSubforums) {
                                            if (subscribed.getSubforumID().equals(subforum.getSubforumID())) {
                                %>
                                        <span class="subscribed-label">🔔</span>
                                <%
                                                break;
                                            }
                                        }
                                    }
                                %>
                                <%= subforum.getName() %>
                            </h3>
                        </div>
            <%
                    }
                }
            %>
        </div>
    </div>


</body>
</html>
