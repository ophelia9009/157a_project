<%--
  home.jsp - Main forum page after login
  Displays navigation and subforum listing
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    </div>

    <%-- Main content section --%>
    <h1>Sub Forum List</h1>


    <script>
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

<%-- sticky version
    <form method="get" action="home.jsp">
        <input type="text" name="filterName" placeholder="Search by Name" value="<%= request.getParameter("filterName") != null ? request.getParameter("filterName") : "" %>">
        <input type="date" name="endDate" placeholder="End Date" value="<%= request.getParameter("endDate") != null ? request.getParameter("endDate") : "" %>">
        <input type="number" name="minSubscribers" placeholder="Min Subscribers" value="<%= request.getParameter("minSubscribers") != null ? request.getParameter("minSubscribers") : "" %>">
        <input type="number" name="maxSubscribers" placeholder="Max Subscribers" value="<%= request.getParameter("maxSubscribers") != null ? request.getParameter("maxSubscribers") : "" %>">
        <input type="date" name="lastUpdatedStart" placeholder="Last Updated Start" value="<%= request.getParameter("lastUpdatedStart") != null ? request.getParameter("lastUpdatedStart") : "" %>">
        <input type="date" name="lastUpdatedEnd" placeholder="Last Updated End" value="<%= request.getParameter("lastUpdatedEnd") != null ? request.getParameter("lastUpdatedEnd") : "" %>">
        <button type="submit">Filter</button>
    </form> -->

    <form method="get" action="/api/subforumsearch">
        <input type="text" name="filterName" placeholder="Search by Name">
        <input type="date" name="minCreationDate" placeholder="min creation date">
        <input type="date" name="maxCreationDate" placeholder="Max creation date">
        <input type="number" name="minSubscribers" placeholder="Min Subscribers">
        <input type="number" name="maxSubscribers" placeholder="Max Subscribers">
        <input type="date" name="minLastUpdated" placeholder="min last Updated">
        <input type="date" name="maxLastUpdated" placeholder="max last End">
        <button type="submit">Filter</button>
    </form>


</body>
</html>
