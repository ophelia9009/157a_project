<%@ page import="edu.sjsu.cs157a.forum.dao.SubforumDAO" %>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
        subforum = dao.findByPrimaryKey("subforums", "SubforumID", subforumId);
    } catch (Exception e) {
        out.println("<p style='color:red;'>Invalid or missing subforum ID.</p>");
        return;
    }

    if (subforum == null) {
        out.println("<p style='color:red;'>Subforum not found.</p>");
        return;
    }

    int userId = user.getUserID();
%>

<h1>Create a Post in: <%= subforum.get("Name") %></h1>

<form method="post" action="${pageContext.request.contextPath}/api/posts">
    <input type="hidden" name="subforumId" value="<%= subforumId %>">
    <input type="hidden" name="userId" value="<%= userId %>">

    <label>Title:<br>
        <input type="text" name="title" required>
    </label><br><br>

    <label>Body:<br>
        <textarea name="bodyText" rows="5" cols="40" required></textarea>
    </label><br><br>

    <button type="submit">Create Post</button>
</form>
