package edu.sjsu.cs157a.forum.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.sjsu.cs157a.forum.dao.CommentDAO;
import edu.sjsu.cs157a.forum.model.Comment;
import edu.sjsu.cs157a.forum.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/commentAction")
public class CommentActionServlet extends BaseServlet {
    private final CommentDAO commentDAO = new CommentDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        setCorsHeaders(response);
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
            return;
        }

        // Check if this is a JSON request (comment creation) or form submission (comment deletion)
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            // Handle comment creation via JSON
            handleCommentCreation(request, response, user);
        } else {
            // Handle comment deletion via form
            handleCommentAction(request, response, user);
        }
    }
    
    private void handleCommentCreation(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            // Parse the request body
            BufferedReader reader = request.getReader();
            JsonObject jsonRequest = gson.fromJson(reader, JsonObject.class);
            
            // Extract parameters
            Long postId = jsonRequest.get("postId").getAsLong();
            String commentText = jsonRequest.get("commentText").getAsString();
            
            // Create the comment
            Comment comment = commentDAO.createComment(commentText, user.getUserID(), postId);
            
            // Return success response
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("commentId", comment.getCommentID());
            
            out.print(gson.toJson(jsonResponse));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("message", e.getMessage());
            out.print(gson.toJson(error));
        }
    }
    
    private void handleCommentAction(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        String action = request.getParameter("action");
        Long commentId = Long.valueOf(request.getParameter("commentId"));
        
        try {
            if ("delete".equals(action)) {
                boolean deleted = commentDAO.deleteComment(commentId, user.getUserID());
                if (deleted) {
                    // Redirect back to the same post view
                    String postId = request.getParameter("postId");
                    response.sendRedirect(request.getContextPath() + 
                        "/forum/viewpost.jsp?postId=" + postId);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, 
                        "Comment not found or not authorized");
                }
            } else if ("edit".equals(action)) {
                String newText = request.getParameter("commentText");
                if (newText == null || newText.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Comment text cannot be empty");
                    return;
                }
                
                try {
                    Comment updatedComment = commentDAO.updateComment(commentId, user.getUserID(), newText);
                    if (updatedComment != null) {
                        // Redirect back to the same post view
                        String postId = request.getParameter("postId");
                        response.sendRedirect(request.getContextPath() + 
                            "/forum/viewpost.jsp?postId=" + postId);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, 
                            "Comment not found or not authorized");
                    }
                } catch (SQLException e) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error updating comment: " + e.getMessage());
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "Invalid action");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Error processing request: " + e.getMessage());
        }
    }
}
