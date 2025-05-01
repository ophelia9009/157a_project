package com.example.servelet;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/users/*")
public class UserProfileServlet extends HttpServlet {
    UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID required");
            return;
        }

        String userId = pathInfo.substring(1); // Remove leading slash
        User user = userDAO.getUserById(userId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"User not found\"}");
        } else {
            response.getWriter().write(gson.toJson(user));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || !pathInfo.equals("/register")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            User newUser = gson.fromJson(request.getReader(), User.class);
            User createdUser = userDAO.createUser(newUser);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(createdUser));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Registration failed\",\"details\":\"" +
                e.getMessage() + "\"}");
        }
    }
}