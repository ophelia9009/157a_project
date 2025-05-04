<%--
  index.jsp - Main landing page for the forum application
  Displays welcome message and provides navigation to login/signup pages
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Welcome to Forum</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/flatly/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navigation bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="#">Forum</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="forum/home.jsp">Continue As Guest</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="auth/login.jsp">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="auth/signup.jsp">Sign Up</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main welcome content section -->
    <div class="container mt-5 text-center">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-lg">
                    <div class="card-body p-5">
                        <h1 class="display-4 mb-4">Welcome to Our Forum</h1>
                        <p class="lead">Join the discussion with our community</p>
                        <div class="mt-4">
                            <a href="auth/signup.jsp" class="btn btn-primary btn-lg me-2">Sign Up</a>
                            <a href="auth/login.jsp" class="btn btn-outline-secondary btn-lg">Login</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
