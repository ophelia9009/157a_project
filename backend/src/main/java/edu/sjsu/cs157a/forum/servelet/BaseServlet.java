package edu.sjsu.cs157a.forum.servelet;

import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends jakarta.servlet.http.HttpServlet {
    
    protected void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}