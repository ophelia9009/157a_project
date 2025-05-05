package edu.sjsu.cs157a.forum.servlet;

import edu.sjsu.cs157a.forum.dao.UserDAO;
import edu.sjsu.cs157a.forum.model.Element;
import edu.sjsu.cs157a.forum.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet("/api/profileAction")
public class ProfileActionServlet extends BaseServlet {


    private static final Logger logger = LogManager.getLogger(UserDAO.class);


    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.getWriter().write("ProfileActionServlet is active");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);

        String action = request.getParameter("action");
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        logger.info("Action: " + action + ", userId: " + userId);
        HttpSession session = request.getSession();

        if (userId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID required");
            return;
        }

        try {
            switch (action) {
                case "delete":
                    boolean deleted = userDAO.deleteTuple(new Element("Users", "UserID", userId,new String[]{}, new Object[]{}));
                    if (deleted) {
                        session.invalidate();
                        response.sendRedirect("../index.jsp");
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    }
                    break;

                case "edit":
                    response.sendRedirect(request.getContextPath() + "/user/editprofile.jsp");
                    break;

                case "editSubmit":
                    String newUsername = request.getParameter("username");
                    String newEmail = request.getParameter("email");
                    if (newUsername == null || newUsername.trim().isEmpty() ||
                            newEmail == null || newEmail.trim().isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and Email are required.");
                        return;
                    }
                    userDAO.updateTuple(new Element("Users", "UserID", userId,
                            new String[] { "Username", "Email" },
                            new Object[] { newUsername, newEmail }));

                    User updatedUser = userDAO.getUserById(userId);
                    session.setAttribute("user", updatedUser);

                    response.sendRedirect("../user/myprofile.jsp");
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Error processing request: " + e.getMessage());
        }
    }
    @Override
    public void init() {
        logger.info("ProfileActionServlet initialized");
    }

}