package edu.sjsu.cs157a.forum.servlet;

import com.google.gson.Gson;
import edu.sjsu.cs157a.forum.dao.SubforumDAO;
import edu.sjsu.cs157a.forum.model.Post;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/subforums/*") //  expecsts /api/subforums/{subforumId}/posts
public class SubforumPostsServlet extends BaseServlet {
    private final Gson gson = new Gson();
    private final SubforumDAO subforumDAO = new SubforumDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo(); // e.g. "/3/posts"
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        System.out.println ("SOMETHING HAPPENED");
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

        int subforumId;
        try {
            subforumId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subforum ID: must be a number");
            return;
        }

        List<Post> posts = subforumDAO.getAllSubforumPosts(subforumId);
        response.getWriter().write(gson.toJson(posts));

//        System.out.println (gson.toJson(posts));
    }
}
