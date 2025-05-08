package edu.sjsu.cs157a.forum.servlet;

import com.google.gson.Gson;
import edu.sjsu.cs157a.forum.dao.SubforumDAO;
import edu.sjsu.cs157a.forum.dao.UserDAO;
import edu.sjsu.cs157a.forum.model.Post;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/api/subforums/*") //  expecsts /api/subforums/{subforumId}/posts
public class SubforumPostsServlet extends BaseServlet {


    private static final Logger logger = LogManager.getLogger(UserDAO.class);


    private final Gson gson = new Gson();
    private final SubforumDAO subforumDAO = new SubforumDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo(); // e.g. "/3/posts"
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        logger.info ("SOMETHING HAPPENED");
        if (pathInfo == null || pathInfo.split("/").length != 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected path format: /{subforumId}/posts");
            return;
        }

        String[] parts = pathInfo.split("/"); // ["", "3", "posts"]
        String idStr = parts[1];
        String resource = parts[2];

        if (!resource.equals("posts")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unsupported resource: " + resource);
            return;
        }

        Long subforumId;
        try {
            subforumId = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subforum ID: must be a number");
            return;
        }

        List<Post> posts = subforumDAO.getAllSubforumPosts(subforumId);
        response.getWriter().write(gson.toJson(posts));

    }
}
