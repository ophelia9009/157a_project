package edu.sjsu.cs157a.forum.servelet;

import edu.sjsu.cs157a.forum.dao.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/profileAction")
public class ProfileActionServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("action");
        String userId = request.getParameter("userId");
        HttpSession session = request.getSession();

        if (userId == null || userId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID required");
            return;
        }

        try {
            switch (action) {
                case "delete":
                    boolean deleted = userDAO.deleteUserById(userId);
                    if (deleted) {
                        session.invalidate();
                        response.sendRedirect("../index.jsp");
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    }
                    break;

                case "edit":
                    // TODO: Implement edit functionality
                    response.sendRedirect("myprofile.jsp");
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error processing request: " + e.getMessage());
        }
    }
}