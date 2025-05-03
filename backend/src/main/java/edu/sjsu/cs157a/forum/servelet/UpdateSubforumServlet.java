package edu.sjsu.cs157a.forum.servelet;

import edu.sjsu.cs157a.forum.dao.SubforumDAO;
import edu.sjsu.cs157a.forum.model.Subforum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/forum/updateSubforum")
public class UpdateSubforumServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int subforumId = Integer.parseInt(request.getParameter("subforumId"));
            int userId = Integer.parseInt(request.getParameter("userId"));
            String newDescription = request.getParameter("description");
            
            SubforumDAO subforumDAO = new SubforumDAO();
            subforumDAO.updateSubforum(subforumId, newDescription, userId);
            
            response.sendRedirect(request.getContextPath() + "/user/myprofile.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update subforum: " + e.getMessage());
            request.getRequestDispatcher("/user/myprofile.jsp").forward(request, response);
        }
    }
}