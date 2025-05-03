<%-- editprofile.jsp - User profile editing page --%>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
    return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <link rel="stylesheet" href="$../css/styles.css">
</head>
<body>
    <h1>Edit Your Profile</h1>
    <form method="post" action="${pageContext.request.contextPath}/api/profileAction">
        <input type="hidden" name="action" value="editSubmit">
        <input type="hidden" name="userId" value="<%= user.getUserID() %>">

        <label>Username:
            <input type="text" name="username" value="<%= user.getUsername() %>" required>
        </label><br>

        <label>Email:
            <input type="email" name="email" value="<%= user.getEmail() %>" required>
        </label><br>

        <button type="submit">Save Changes</button>
    </form>
</body>
</html>
