<%-- editpost.jsp - Post editing page --%>
<%@ page import="edu.sjsu.cs157a.forum.model.Post" %>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
    return;
}
Post post = (Post) session.getAttribute("post");
if (post == null) {
    response.sendRedirect(request.getContextPath() + "/forum/home.jsp");
    return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Post</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <h1>Edit Your Post</h1>
    <form method="post" action="${pageContext.request.contextPath}/api/postAction">
        <input type="hidden" name="action" value="editSubmit">
        <input type="hidden" name="postId" value="<%= post.getPostID() %>">

        <label>Title:
            <input type="text" name="title" value="<%= post.getTitle() %>" required>
        </label><br>

        <label>BodyText:
            <input type="text" name="bodyText" value="<%= post.getBodyText() %>" required>
        </label><br>

        <button type="submit">Save Changes</button>
    </form>
</body>
</html>
