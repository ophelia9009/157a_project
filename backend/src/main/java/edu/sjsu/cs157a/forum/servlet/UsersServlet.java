package edu.sjsu.cs157a.forum.servlet;

import edu.sjsu.cs157a.forum.dao.UserDAO;
import edu.sjsu.cs157a.forum.model.User;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/api/users")
public class UsersServlet extends BaseServlet {
    private static final Logger logger = LogManager.getLogger(UsersServlet.class);


    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);
        List<User> users = userDAO.getAllUsers();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(users));
    }
}