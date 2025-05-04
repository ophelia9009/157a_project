<%--
  subforumview.jsp - view a subforum
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import required DAOs and models --%>
<%@ page import="edu.sjsu.cs157a.forum.dao.UserDAO, edu.sjsu.cs157a.forum.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="../css/styles.css">

    <script>
        async function fetchPosts () {
            const subforumID = document.getElementById("tempsubID").value;

            try {
                const response = await fetch (`/api/subforums/${subforumID}/posts`);
                if (!response.ok) {
                    console.log("couldn't get posts");
                }
                const posts = await response.json();
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
                        <th>Title</th>
                        <th>Body</th>
                        <th>Creation Date</th>
                        <th>Rating</th>
                    </tr>
                </thead>
                <tbody>
                    ${posts.map(post => `
                        <tr>
                            <td>${post.title}</td>
                            <td>${post.bodytext}</td>
                            <td>${new Date(post.creationDate).toLocaleString()}</td>
                            <td>${post.rating}</td>
                        </tr>
                    `).join('')}
                </tbody>
            `;

            container.appendChild(table);
        }
        document.getElementById("subforumForm").addEventListener("submit", function(event) {
            event.preventDefault(); // Prevent page reload
            fetchPosts();
        });
    </script>
</head>
<body>
    <div class="nav-links">
        <a href="../forum/home.jsp">Home</a>
    </div>

    <h3> Subforum view </h3>
    <p> Ill get rid of this later but for now this thing controls which sub</p>

    <form id="subforumForm">
        <input type="text" name="tempsubID" placeholder="Subforum ID" id="tempsubID" required />
        <button type="submit">Load Posts</button>
    </form>

    <div id="postsContainer">
    </div>
</body>
</html>
