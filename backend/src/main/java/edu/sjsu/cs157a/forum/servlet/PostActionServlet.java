package edu.sjsu.cs157a.forum.servlet;

import edu.sjsu.cs157a.forum.dao.PostDAO;
import edu.sjsu.cs157a.forum.model.Element;
import edu.sjsu.cs157a.forum.model.Post;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/api/posts/*")
public class PostActionServlet extends BaseServlet {
    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.getWriter().write("PostActionServlet is active");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);

        String action = request.getParameter("action");
        Integer postId = Integer.valueOf(request.getParameter("postId"));
        if (postId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Post ID required");
            return;
        }
        System.out.println("Action: " + action + ", postId: " + postId);
        HttpSession session = request.getSession();



        try {
            switch (action) {
                case "delete":
                    boolean deleted = postDAO.deleteTuple(new Element("posts", "PostID", postId,new String[]{}, new Object[]{}));
                    if (deleted) {
                        //maybe this should become the subforum homepage
                        response.sendRedirect("/forum/home/.jsp");
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
                    }
                    break;

                case "edit":
                    response.sendRedirect(request.getContextPath() + "/user/editpost.jsp");
                    break;

                case "editSubmit":
                    String title = request.getParameter("title");
                    String bodyText = request.getParameter("bodyText");
                    if (title.isBlank() && bodyText.isBlank()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Title or Body Text are required.");
                        return;
                    }
                    postDAO.updateTuple(new Element("posts", "PostID", postId,
                            new String[] { "Title", "BodyText" },
                            new Object[] { title, bodyText}));

                    Post updatedPost = postDAO.getPostByID(postId);
                    session.setAttribute("post", updatedPost);

                    response.sendRedirect("../api/posts");
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
        System.out.println("PostActionServlet initialized");
    }

}
