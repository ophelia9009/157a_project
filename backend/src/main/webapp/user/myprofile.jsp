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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Profile</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/flatly/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <!-- Include navigation -->
    <jsp:include page="../common/navigation.jsp">
        <jsp:param name="currentPage" value="profile" />
    </jsp:include>
    <div class="container mt-4">
        <%-- Check user session --%>
        <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
        %>
        
        <%-- Profile display section --%>
        <div class="row">
            <div class="col-md-8 mx-auto">
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-primary text-white">
                        <h2 class="h4 mb-0"><i class="bi bi-person-circle me-2"></i>My Profile</h2>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-4 fw-bold">User ID:</div>
                            <div class="col-md-8"><%= user.getUserID() %></div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4 fw-bold">Username:</div>
                            <div class="col-md-8"><%= user.getUsername() %></div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4 fw-bold">Email:</div>
                            <div class="col-md-8"><%= user.getEmail() %></div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4 fw-bold">Registration Date:</div>
                            <div class="col-md-8"><%= user.getRegisterDate() %></div>
                        </div>
                        
                        <%-- Profile action form --%>
                        <form id="profileForm" method="post" action="${pageContext.request.contextPath}/api/profileAction" class="mt-4">
                            <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                            <div class="d-flex gap-2">
                                <button type="submit" name="action" value="edit" class="btn btn-primary">
                                    <i class="bi bi-pencil-square me-1"></i> Edit Profile
                                </button>
                                <button type="submit" name="action" value="delete" class="btn btn-danger" onclick="return confirmDelete()">
                                    <i class="bi bi-trash me-1"></i> Delete My Account
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <%-- Subforums section --%>
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h2 class="h4 mb-0"><i class="bi bi-collection me-2"></i>My Owned Subforums</h2>
                    </div>
                    <div class="card-body">
                        <%
                        try {
                            SubforumDAO subforumDAO = new SubforumDAO();
                            List<Subforum> ownedSubforums = subforumDAO.getFilteredSubforums(
                                null, null, null, null, null, null, null
                            );
                            
                            boolean hasOwnedSubforums = false;
                            
                            for (Subforum subforum : ownedSubforums) {


                                if (java.util.Objects.equals(subforum.getOwnerID() , user.getUserID())) {
                                    hasOwnedSubforums = true;
                                    break;
                                }
                            }
                            
                            if (!hasOwnedSubforums) {
                        %>
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle me-2"></i>You haven't created any subforums yet.
                            </div>
                        <%
                            } else {
                                for (Subforum subforum : ownedSubforums) {
                                    if (java.util.Objects.equals(subforum.getOwnerID() , user.getUserID())) {
                        %>
                            <div class="card mb-3">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h5 class="card-title"><a href="../forum/subforumview.jsp?subforumId=<%= subforum.getSubforumID() %>" class="text-decoration-none"><%= subforum.getName() %></a></h5>
                                            <p class="card-text text-muted mb-1"><%= subforum.getDescription() %></p>
                                            <small class="text-muted">Created: <%= subforum.getCreationDate() %></small>
                                        </div>
                                        <div class="d-flex gap-2">
                                            <button type="button" class="btn btn-outline-primary edit-btn"
                                                    data-subforum-id="<%= subforum.getSubforumID() %>"
                                                    data-subforum-name="<%= subforum.getName() %>"
                                                    data-subforum-desc="<%= subforum.getDescription() %>">
                                                <i class="bi bi-pencil"></i> Edit
                                            </button>
                                            <button type="button" class="btn btn-outline-danger delete-btn"
                                                    data-subforum-id="<%= subforum.getSubforumID() %>"
                                                    data-subforum-name="<%= subforum.getName() %>">
                                                <i class="bi bi-trash"></i> Delete
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <%
                                    }
                                }
                            }
                        } catch (Exception e) {
                        %>
                            <div class="alert alert-danger">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>Error loading subforums: <%= e.getMessage() %>
                            </div>
                        <%
                        }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <%-- Edit Subforum Modal --%>
    <div class="modal fade" id="editSubforumModal" tabindex="-1" aria-labelledby="editSubforumModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editSubforumModalLabel">Edit Subforum: <span id="editSubforumName"></span></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editSubforumForm">
                        <input type="hidden" id="editSubforumId">
                        <div class="mb-3">
                            <label for="editSubforumDesc" class="form-label">Description:</label>
                            <textarea class="form-control" id="editSubforumDesc" name="description" rows="3" required></textarea>
                        </div>
                        <div class="text-end">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <%-- JavaScript functions --%>
    <script>
    function confirmDelete() {
        return confirm('Are you sure you want to delete your account? This cannot be undone.');
    }

    // Edit Subforum Modal Handling
    const editModal = new bootstrap.Modal(document.getElementById('editSubforumModal'));

    // Set up edit buttons
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const subforumId = this.getAttribute('data-subforum-id');
            const subforumName = this.getAttribute('data-subforum-name');
            const subforumDesc = this.getAttribute('data-subforum-desc');
            
            document.getElementById('editSubforumId').value = subforumId;
            document.getElementById('editSubforumName').textContent = subforumName;
            document.getElementById('editSubforumDesc').value = subforumDesc;
            editModal.show();
        });
    });

    // Set up delete buttons
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const subforumId = this.getAttribute('data-subforum-id');
            const subforumName = this.getAttribute('data-subforum-name');
            
            if (confirm(`Are you sure you want to delete the subforum ` + subforumName + `? This cannot be undone.`)) {
                const userId = '<%= user.getUserID() %>';
                
                fetch('/backend/forum/deleteSubforum', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({
                        subforumId: subforumId,
                        userId: userId
                    })
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to delete subforum');
                    }
                    return response.text();
                })
                .then(() => {
                    // Show success message with Bootstrap toast
                    const toastContainer = document.createElement('div');
                    toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
                    toastContainer.style.zIndex = '11';
                    toastContainer.innerHTML = `
                        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    Subforum deleted successfully!
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    `;
                    document.body.appendChild(toastContainer);
                    const toastEl = toastContainer.querySelector('.toast');
                    const toast = new bootstrap.Toast(toastEl);
                    toast.show();
                    
                    setTimeout(() => window.location.reload(), 1500);
                })
                .catch(error => {
                    // Show error message with Bootstrap toast
                    const toastContainer = document.createElement('div');
                    toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
                    toastContainer.style.zIndex = '11';
                    toastContainer.innerHTML = `
                        <div class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    Error: ${error.message}
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    `;
                    document.body.appendChild(toastContainer);
                    const toastEl = toastContainer.querySelector('.toast');
                    const toast = new bootstrap.Toast(toastEl);
                    toast.show();
                });
            }
        });
    });

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
            // Show success message with Bootstrap toast
            const toastContainer = document.createElement('div');
            toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
            toastContainer.style.zIndex = '11';
            toastContainer.innerHTML = `
                <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="d-flex">
                        <div class="toast-body">
                            Subforum updated successfully!
                        </div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                    </div>
                </div>
            `;
            document.body.appendChild(toastContainer);
            const toastEl = toastContainer.querySelector('.toast');
            const toast = new bootstrap.Toast(toastEl);
            toast.show();
            
            editModal.hide();
            setTimeout(() => window.location.reload(), 1500);
        })
        .catch(error => {
            // Show error message with Bootstrap toast
            const toastContainer = document.createElement('div');
            toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
            toastContainer.style.zIndex = '11';
            toastContainer.innerHTML = `
                <div class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="d-flex">
                        <div class="toast-body">
                            Error: ${error.message}
                        </div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                    </div>
                </div>
            `;
            document.body.appendChild(toastContainer);
            const toastEl = toastContainer.querySelector('.toast');
            const toast = new bootstrap.Toast(toastEl);
            toast.show();
        });
    });
    </script>
</body>
</html>
