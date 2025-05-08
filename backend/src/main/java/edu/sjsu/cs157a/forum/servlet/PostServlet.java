package edu.sjsu.cs157a.forum.servlet;

import com.google.gson.Gson;
import edu.sjsu.cs157a.forum.dao.PostDAO;
import edu.sjsu.cs157a.forum.model.Post;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/api/posts")
public class PostServlet extends BaseServlet {
    private final PostDAO postDAO = new PostDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            String title = request.getParameter("title");
            String bodytext = request.getParameter("bodyText");
            Long UserID = Long.valueOf(request.getParameter("userId"));
            Long SubforumID = Long.valueOf(request.getParameter("subforumId"));

            Post created = postDAO.createPost(title, bodytext, UserID, SubforumID);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(created));

            response.sendRedirect("/backend/forum/subforumview.jsp?subforumId=" + SubforumID);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to create post\"}" + e.getMessage() + "\"}");
        }
    }
}
