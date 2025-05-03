package edu.sjsu.cs157a.forum.servlet;

import edu.sjsu.cs157a.forum.dao.BaseDAO;
import edu.sjsu.cs157a.forum.dao.SubforumDAO;
import edu.sjsu.cs157a.forum.model.Subforum;
import com.google.gson.Gson;
import edu.sjsu.cs157a.forum.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/subforums")
public class SubforumsServlet extends BaseServlet {
    private final BaseDAO baseDAO= new BaseDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Map<String, Object>> subforums = null;
        try {
            subforums = baseDAO.getTable("subforums", new String[]{"SubforumID", "Name", "Description", "CreationDate",
                    "SubscriberCount", "LastUpdated"});
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(subforums));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            // Parse request body
            Map<String, String> requestBody = gson.fromJson(request.getReader(), Map.class);
            String name = requestBody.get("name");
            String description = requestBody.get("description");
            
            // Get ownerID from session
            Integer ownerID = ((User) request.getSession().getAttribute("user")).getUserID();
            if (ownerID == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }

            // Create subforum
            SubforumDAO subforumDAO = new SubforumDAO();
            Subforum subforum = subforumDAO.createSubforum(name, description, ownerID);

            // Return created subforum
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(subforum));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create subforum: " + e.getMessage());
        }
    }
}