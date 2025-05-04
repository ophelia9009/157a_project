package edu.sjsu.cs157a.forum.servlet;

import com.google.gson.Gson;
import edu.sjsu.cs157a.forum.dao.SubforumDAO;
import edu.sjsu.cs157a.forum.model.Post;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/subforums/*") // expects api/subforums/{subforumId}/posts
public class SubforumPostsServlet extends BaseServlet {
//    private final PostDAO postDAO = new PostDAO();
    private final Gson gson = new Gson();
    private final SubforumDAO subforumDAO = new SubforumDAO();

    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subforum ID required");
            return;
        }

        Integer subforumId = Integer.parseInt(pathInfo.substring(1)); // Remove leading slash
        List<Post> posts = subforumDAO.getAllSubforumPosts(subforumId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(posts));
    }
}
