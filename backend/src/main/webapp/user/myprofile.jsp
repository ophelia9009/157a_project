<%--
  myprofile.jsp - User profile management page
  Displays profile information and handles account actions
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.sjsu.cs157a.forum.model.User" %>
<%@ page import="edu.sjsu.cs157a.forum.dao.SubforumDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.sjsu.cs157a.forum.model.Subforum" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <div class="nav-links">
        <a href="../forum/home.jsp">View Forum</a>
        <a href="../index.jsp">Logout</a>
    </div>
    <%-- Check user session --%>
    <%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
        return;
    }
    %>
    <%-- Profile display section --%>
    <h1>My Profile</h1>
    <div class="profile-info">
        <p><strong>User ID:</strong> <%= user.getUserID() %></p>
        <p><strong>Username:</strong> <%= user.getUsername() %></p>
        <p><strong>Email:</strong> <%= user.getEmail() %></p>
        <p><strong>Registration Date:</strong> <%= user.getRegisterDate() %></p>
    </div>

    <%-- Profile action form --%>
    <form id="profileForm" method="post" action="${pageContext.request.contextPath}/api/profileAction">
        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
        <button type="submit" name="action" value="edit">Edit Profile</button>
        <button type="submit" name="action" value="delete" class="delete-btn" onclick="return confirmDelete()">Delete My Account</button>
    </form>
</div>

<%-- Subforums section --%>
<div class="profile-section">
    <h2>My Owned Subforums</h2>
    <div class="profile-info">
    <%
    try {
        SubforumDAO subforumDAO = new SubforumDAO();
        List<Subforum> ownedSubforums = subforumDAO.getFilteredSubforums(
            null, null, null, null, null, null, null
        );
        
        if (ownedSubforums.isEmpty()) {
    %>
        <p>You haven't created any subforums yet.</p>
    <%
        } else {
            for (Subforum subforum : ownedSubforums) {
                if (subforum.getOwnerID() == user.getUserID()) {
    %>
        <div class="subforum-item" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <div>
                <strong><%= subforum.getName() %></strong> -
                <%= subforum.getDescription() %>
                (Created: <%= subforum.getCreationDate() %>)
            </div>
            <form method="get" action="../forum/editSubforum.jsp" style="margin-left: 20px;">
                <input type="hidden" name="subforumId" value="<%= subforum.getSubforumID() %>">
                <button type="submit" class="edit-btn">Edit Subforum</button>
            </form>
        </div>
    <%
                }
            }
        }
    } catch (Exception e) {
    %>
        <p class="error">Error loading subforums: <%= e.getMessage() %></p>
    <%
    }
    %>
    </div>
</div>
    
    <%-- Edit Subforum Modal --%>
    <div id="editSubforumModal" style="display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.4);">
        <div style="background-color: #fefefe; margin: 15% auto; padding: 20px; border: 1px solid #888; width: 50%;">
            <span class="close-edit-modal" style="float: right; cursor: pointer;">&times;</span>
            <h2>Edit Subforum: <span id="editSubforumName"></span></h2>
            <form id="editSubforumForm">
                <input type="hidden" id="editSubforumId">
                <label for="editSubforumDesc">Description:</label>
                <textarea id="editSubforumDesc" name="description" required></textarea><br><br>
                <button type="submit">Save Changes</button>
                <button type="button" class="cancel-edit-btn">Cancel</button>
            </form>
        </div>
    </div>

    <%-- JavaScript functions --%>
    <script>
    function confirmDelete() {
        return confirm('Are you sure you want to delete your account? This cannot be undone.');
    }

    // Edit Subforum Modal Handling
    const editModal = document.getElementById('editSubforumModal');
    const closeEditModal = document.querySelector('.close-edit-modal');
    const cancelEditBtn = document.querySelector('.cancel-edit-btn');

    // Set up edit buttons
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const subforumId = this.closest('form').querySelector('input[name="subforumId"]').value;
            const subforumName = this.closest('.subforum-item').querySelector('strong').textContent;
            const subforumDesc = this.closest('.subforum-item').querySelector('div').textContent
                .split('-')[1]
                .split('(')[0]
                .trim();
            
            document.getElementById('editSubforumId').value = subforumId;
            document.getElementById('editSubforumName').textContent = subforumName;
            document.getElementById('editSubforumDesc').value = subforumDesc;
            editModal.style.display = 'block';
        });
    });

    closeEditModal.onclick = function() {
        editModal.style.display = 'none';
    }

    cancelEditBtn.onclick = function() {
        editModal.style.display = 'none';
    }

    window.onclick = function(event) {
        if (event.target == editModal) {
            editModal.style.display = 'none';
        }
    }

    // Handle Subforum Update
    document.getElementById('editSubforumForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const subforumId = document.getElementById('editSubforumId').value;
        const description = document.getElementById('editSubforumDesc').value;
        const userId = '<%= user.getUserID() %>';
        
        fetch('/backend/forum/updateSubforum', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                subforumId: subforumId,
                description: description,
                userId: userId
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update subforum');
            }
            return response.text();
        })
        .then(() => {
            alert('Subforum updated successfully!');
            editModal.style.display = 'none';
            window.location.reload();
        })
        .catch(error => {
            alert('Error: ' + error.message);
        });
    });
    </script>
</body>
</html>