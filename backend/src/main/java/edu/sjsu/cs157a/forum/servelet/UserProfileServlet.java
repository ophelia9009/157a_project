package edu.sjsu.cs157a.forum.servelet;

import edu.sjsu.cs157a.forum.dao.UserDAO;
import edu.sjsu.cs157a.forum.model.User;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/users/*")
public class UserProfileServlet extends BaseServlet {
    UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);
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
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            switch (pathInfo) {
                case "/register":
                    User newUser = gson.fromJson(request.getReader(), User.class);
                    User createdUser = userDAO.createUser(newUser);
                    
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getWriter().write(gson.toJson(createdUser));
                    break;
                    
                case "/login":
                    User loginUser = gson.fromJson(request.getReader(), User.class);
                    String storedPassword = userDAO.getPasswordByUsername(loginUser.getUsername());
                    
                    if (storedPassword != null && storedPassword.equals(loginUser.getPassword())) {
                        User user = userDAO.getUserByUsername(loginUser.getUsername());
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(gson.toJson(user));
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\":\"Invalid credentials\"}");
                    }
                    break;
                    
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Operation failed\",\"details\":\"" +
                e.getMessage() + "\"}");
        }
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID required");
            return;
        }

        String userId = pathInfo.substring(1); // Remove leading slash
        
        try {
            // Get existing user data
            User existingUser = userDAO.getUserById(userId);
            if (existingUser == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            // Parse update fields (only password and email should be updated)
            User updateFields = gson.fromJson(request.getReader(), User.class);
            
            // Create updated user with existing data plus new password/email
            User updatedUser = new User(
                userId,
                existingUser.getUsername(), // Keep original username
                updateFields.getPassword() != null ? updateFields.getPassword() : existingUser.getPassword(),
                updateFields.getEmail() != null ? updateFields.getEmail() : existingUser.getEmail(),
                existingUser.getRegisterDate() // Keep original registration date
            );
            
            User result = userDAO.updateUser(updatedUser);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Update failed\",\"details\":\"" +
                e.getMessage() + "\"}");
        }
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID required");
            return;
        }

        String userId = pathInfo.substring(1); // Remove leading slash
        
        try {
            boolean deleted = userDAO.deleteUserById(userId);
            
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"User not found\"}");
            }
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Delete failed\",\"details\":\"" +
                e.getMessage() + "\"}");
        }
    }
}