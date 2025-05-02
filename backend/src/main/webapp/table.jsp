<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.dao.*, java.util.List, java.util.Map, edu.sjsu.cs157a.forum.model.Element, java.sql.Timestamp, java.time.Instant"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forum Tables</title>
    <style>
        table {
            border-collapse: collapse;
            width: 90%;
            margin: 20px auto;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        h1 {
            text-align: center;
        }
    </style>
</head>
<div style="text-align: center; margin: 20px;">
    <a href="?action=create">Create Comments</a> |
    <a href="?action=update">Update Comments</a> |
    <a href="?action=delete">Delete Comments</a> |
    <a href="?action=reset">Reset Flags</a>
</div>
<%
    // Reset flags if requested
    if ("reset".equals(request.getParameter("action"))) {
        session.removeAttribute("created");
        session.removeAttribute("updated");
        session.removeAttribute("deleted");
    }
%>
<body>

    <%
        BaseDAO dao = new BaseDAO();
        UserDAO userDAO = new UserDAO();
    %>

    <!-- USERS -->
    <h1>User List</h1>
    <%
        List<Map<String, Object>> users = userDAO.getTable("users", new String[]{"UserID", "Username", "Email", "RegisterDate"});
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Register Date</th>
        </tr>
        <% for(Map<String, Object> user : users) { %>
        <tr>
            <td><%= user.get("UserID") %></td>
            <td><%= user.get("Username") %></td>
            <td><%= user.get("Email") %></td>
            <td><%= user.get("RegisterDate") %></td>
        </tr>
        <% } %>
    </table>

    <!-- SUBFORUMS -->
    <h1>Subforum List</h1>
    <%
        List<Map<String, Object>> subforums = dao.getTable("subforums", new String[]{
            "SubforumID", "Name", "Description", "CreationDate", "SubscriberCount", "LastUpdated"
        });
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Creation Date</th>
            <th>Subscriber Count</th>
            <th>Last Updated</th>
        </tr>
        <% for(Map<String, Object> sf : subforums) { %>
        <tr>
            <td><%= sf.get("SubforumID") %></td>
            <td><%= sf.get("Name") %></td>
            <td><%= sf.get("Description") %></td>
            <td><%= sf.get("CreationDate") %></td>
            <td><%= sf.get("SubscriberCount") %></td>
            <td><%= sf.get("LastUpdated") %></td>
        </tr>
        <% } %>
    </table>

    <!-- POSTS -->
    <h1>Post List</h1>
    <%
        List<Map<String, Object>> posts = dao.getTable("posts", new String[]{
            "PostID", "Title", "BodyText", "CreationDate", "Rating", "UserID", "SubforumID"
        });
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Body</th>
            <th>Created</th>
            <th>Rating</th>
            <th>Author ID</th>
            <th>Subforum ID</th>
        </tr>
        <% for(Map<String, Object> post : posts) { %>
        <tr>
            <td><%= post.get("PostID") %></td>
            <td><%= post.get("Title") %></td>
            <td><%= post.get("BodyText") %></td>
            <td><%= post.get("CreationDate") %></td>
            <td><%= post.get("Rating") %></td>
            <td><%= post.get("UserID") %></td>
            <td><%= post.get("SubforumID") %></td>
        </tr>
        <% } %>
    </table>

    <!-- COMMENTS -->
    <h1>Comment List</h1>
    <%
        List<Map<String, Object>> comments = dao.getTable("comments", new String[]{
            "CommentID", "CommentText", "CreationDate", "UserID", "PostID", "ParentID", "LastUpdated"});
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Text</th>
            <th>Created</th>
            <th>User ID</th>
            <th>Post ID</th>
            <th>Parent ID</th>
            <th>Last Updated</th>
        </tr>
        <% for(Map<String, Object> comment : comments) { %>
        <tr>
            <td><%= comment.get("CommentID") %></td>
            <td><%= comment.get("CommentText") %></td>
            <td><%= comment.get("CreationDate") %></td>
            <td><%= comment.get("UserID") %></td>
            <td><%= comment.get("PostID") %></td>
            <td><%= comment.get("ParentID") %></td>
            <td><%= comment.get("LastUpdated") %></td>
        </tr>
        <% } %>
    </table>
    <!-- SUBSCRIPTIONS -->
    <h1>Subscription List</h1>
    <%
        List<Map<String, Object>> subscriptions = dao.getTable("subscriptions", new String[]{
            "UserID", "SubforumID", "SubscriptionDate"
        });
    %>
    <table>
        <tr>
            <th>User ID</th>
            <th>Subforum ID</th>
            <th>Subscription Date</th>
        </tr>
        <% for(Map<String, Object> sub : subscriptions) { %>
        <tr>
            <td><%= sub.get("UserID") %></td>
            <td><%= sub.get("SubforumID") %></td>
            <td><%= sub.get("SubscriptionDate") %></td>
        </tr>
        <% } %>
    </table>
    <p style="text-align: center; color: green;">✔ SELECT works.</p>
    <!-- COMMENTS UPDATED-->
        <h1>Comment List with Updated Comments</h1>
        <%

            if ("update".equals(request.getParameter("action")) && session.getAttribute("updated") == null) {
                dao.updateTuple(new Element ("comments", "CommentID", 1, new String[] {"CommentText", "LastUpdated"},
                                        new Object[] {"I take it back. Mew FTW.", Timestamp.from(Instant.now())}));
                dao.updateTuple(new Element ("comments", "CommentID", 2, new String[] {"CommentText", "LastUpdated"},
                                        new Object[] {"Mew is actually Just Better.", Timestamp.from(Instant.now())}));
                session.setAttribute("updated", true);
            }
            comments = dao.getTable("comments", new String[]{
                        "CommentID", "CommentText", "CreationDate", "UserID", "PostID", "ParentID", "LastUpdated"});
        %>
        <table>
            <tr>
                <th>ID</th>
                <th>Text</th>
                <th>Created</th>
                <th>User ID</th>
                <th>Post ID</th>
                <th>Parent ID</th>
                <th>Last Updated</th>
            </tr>
            <% for(Map<String, Object> comment : comments) { %>
            <tr>
                <td><%= comment.get("CommentID") %></td>
                <td><%= comment.get("CommentText") %></td>
                <td><%= comment.get("CreationDate") %></td>
                <td><%= comment.get("UserID") %></td>
                <td><%= comment.get("PostID") %></td>
                <td><%= comment.get("ParentID") %></td>
                <td><%= comment.get("LastUpdated") %></td>
            </tr>
            <% } %>
        </table>
    <!-- COMMENTS DELETED-->
        <h1>Comment List with Deleted Comments</h1>
            <%
                String action = request.getParameter("action");

                if ("delete".equals(request.getParameter("action")) && session.getAttribute("deleted") == null)  {
                    List<Map<String, Object>> toDelete = dao.getTable("comments", new String[]{"CommentID"}, 2);
                    for (Map<String, Object> row : toDelete) {
                        dao.deleteTuple(new Element("comments", "CommentID", row.get("CommentID"), new String[]{}, new Object[]{}));
                    }
                    session.setAttribute("deleted", true);
                }
            %>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Text</th>
                    <th>Created</th>
                    <th>User ID</th>
                    <th>Post ID</th>
                    <th>Parent ID</th>
                    <th>Last Updated</th>
                </tr>
                <% for(Map<String, Object> comment : comments) { %>
                <tr>
                    <td><%= comment.get("CommentID") %></td>
                    <td><%= comment.get("CommentText") %></td>
                    <td><%= comment.get("CreationDate") %></td>
                    <td><%= comment.get("UserID") %></td>
                    <td><%= comment.get("PostID") %></td>
                    <td><%= comment.get("ParentID") %></td>
                    <td><%= comment.get("LastUpdated") %></td>
                </tr>
                <% } %>
            </table>

    <!-- COMMENTS CREATED-->
        <h1>Comment List with Created Comments</h1>
        <%
            if ("create".equals(request.getParameter("action")) && session.getAttribute("created") == null) {
                Element c1 = dao.createTuple(new Element("comments", "CommentID", null,
                    new String[]{"CommentText", "UserID", "PostID", "ParentID"},
                    new Object[]{"Umm Mew can create stuff", 10, 1, 1}));
                dao.updateTuple(new Element("comments", "CommentID", c1.getPrimaryKeyValue(),
                    new String[]{"ParentID"}, new Object[]{c1.getPrimaryKeyValue()}));

                Element c2 = dao.createTuple(new Element("comments", "CommentID", null,
                    new String[]{"CommentText", "UserID", "PostID", "ParentID"},
                    new Object[]{"Umm Mewtwo can create stuff", 11, 1, 1}));
                dao.updateTuple(new Element("comments", "CommentID", c2.getPrimaryKeyValue(),
                    new String[]{"ParentID"}, new Object[]{c2.getPrimaryKeyValue()}));

                session.setAttribute("created", true);
            }

            // Fetch updated comment list
            comments = dao.getTable("comments", new String[]{
                "CommentID", "CommentText", "CreationDate", "UserID", "PostID", "ParentID", "LastUpdated"});
        %>

        <table>
            <tr>
                <th>ID</th>
                <th>Text</th>
                <th>Created</th>
                <th>User ID</th>
                <th>Post ID</th>
                <th>Parent ID</th>
                <th>Last Updated</th>
            </tr>
            <% for(Map<String, Object> comment : comments) { %>
            <tr>
                <td><%= comment.get("CommentID") %></td>
                <td><%= comment.get("CommentText") %></td>
                <td><%= comment.get("CreationDate") %></td>
                <td><%= comment.get("UserID") %></td>
                <td><%= comment.get("PostID") %></td>
                <td><%= comment.get("ParentID") %></td>
                <td><%= comment.get("LastUpdated") %></td>
            </tr>
            <% } %>
        </table>

</body>
</html>
