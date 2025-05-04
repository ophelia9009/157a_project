package edu.sjsu.cs157a.forum.servlet;

import edu.sjsu.cs157a.forum.dao.CommentDAO;
import edu.sjsu.cs157a.forum.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/api/commentAction")
public class CommentActionServlet extends BaseServlet {
    private final CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        setCorsHeaders(response);
        
        String action = request.getParameter("action");
        Integer commentId = Integer.valueOf(request.getParameter("commentId"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
            return;
        }

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