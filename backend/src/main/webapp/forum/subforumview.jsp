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
    <title>Subforum View</title>
    <link rel="stylesheet" href="../css/styles.css">

    <script>
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
                container.innerHTML = "<p>No posts available.</p>";
                return;
            }
            const table = document.createElement('table');
            table.border = '1';
            table.innerHTML = `
                <thead>
                    <tr>
                        <th>Post ID</th>
                        <th>Title</th>
                        <th>Body</th>
                        <th>Creation Date</th>
                        <th>Rating</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            `;

            const tbody = table.querySelector('tbody');
            for (let i = 0; i < posts.length; i++) {
                const post = posts[i];
                const row = document.createElement('tr');
                row.innerHTML = "" +
                    "<td>" + post.postID + "</td>" +
                    "<td>" + post.title + "</td>" +
                    "<td>" + post.bodytext + "</td>" +
                    "<td>" + post.creationDate + "</td>" +
                    "<td>" + post.rating + "</td>" ;

                tbody.appendChild(row);
            }

            container.appendChild(table);
        }

        window.addEventListener("DOMContentLoaded", function () {
            fetchPosts("<%= subforumId %>");
        });
    </script>
</head>
<body>
    <div class="nav-links">
        <a href="../forum/home.jsp">Home</a>
    </div>

    <h3> Subforum view </h3>
    <p>Viewing posts for subforum ID: <%= subforumId %></p>

    <div id="postsContainer">
    </div>
</body>
</html>
