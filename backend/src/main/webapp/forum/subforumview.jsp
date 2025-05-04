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
    <title>Subforum View</title>
    <link rel="stylesheet" href="../css/styles.css">

    <script>
        async function fetchPosts () {
            const subforumID = document.getElementById("tempsubID").value.trim();
            console.log("Subforum ID is:", subforumID);
            try {
                // const response = await fetch(`/backend/api/subforums/${subforumID}/posts`);
                const response = await fetch(`http://localhost:8080/backend/api/subforums/1/posts`);
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

            const preElement = document.createElement('pre');
            preElement.textContent = posts;

            container.appendChild(preElement);

            // container.innerHTML = `<pre>${JSON.stringify(posts, null, 2)}</pre>`;
        }

        window.addEventListener("DOMContentLoaded", function () {
            document.getElementById("subforumForm").addEventListener("submit", function (event) {
                event.preventDefault(); // Prevent page reload
                fetchPosts();
            });
        });
    </script>
</head>
<body>
    <div class="nav-links">
        <a href="../forum/home.jsp">Home</a>
    </div>

    <h3> Subforum view </h3>
    <p> Ill get rid of this later but for now this thing controls which sub</p>
    <p> actually it just displays the subforum id 1 </p>

    <form id="subforumForm">
        <input type="text" name="tempsubID" placeholder="Subforum ID" id="tempsubID" required />
        <button type="submit">Load Posts</button>
    </form>

    <div id="postsContainer">
    </div>
</body>
</html>
