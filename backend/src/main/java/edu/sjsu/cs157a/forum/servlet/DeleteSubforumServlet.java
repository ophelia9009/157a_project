package edu.sjsu.cs157a.forum.servlet;

import edu.sjsu.cs157a.forum.dao.SubforumDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/forum/deleteSubforum")
public class DeleteSubforumServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long subforumId = Long.parseLong(request.getParameter("subforumId"));
            Long userId = Long.parseLong(request.getParameter("userId"));
            
            SubforumDAO subforumDAO = new SubforumDAO();
            subforumDAO.deleteSubforum(subforumId);
            
            response.sendRedirect(request.getContextPath() + "/user/myprofile.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to delete subforum: " + e.getMessage());
            request.getRequestDispatcher("/user/myprofile.jsp").forward(request, response);
        }
    }
}