package edu.sjsu.cs157a.forum.servlet;

import edu.sjsu.cs157a.forum.dao.PostDAO;
import edu.sjsu.cs157a.forum.model.Element;
import edu.sjsu.cs157a.forum.model.Post;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/api/postAction")
public class PostActionServlet extends BaseServlet {

    private static final Logger logger = LogManager.getLogger(PostActionServlet.class);


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
        Integer subforumId = Integer.valueOf(request.getParameter("subforumId"));
        logger.info("Action: " + action + ", postId: " + postId);
        HttpSession session = request.getSession();

        try {
            switch (action) {
                case "delete":
                    boolean deleted = postDAO.deleteTuple(new Element("Posts", "PostID", postId,new String[]{}, new Object[]{}));
                    if (deleted) {
                        //maybe this should become the subforum homepage
                        response.sendRedirect("/backend/forum/subforumview.jsp?subforumId=" + subforumId);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
                    }
                    break;

                case "edit":
                    Post post = postDAO.getPostByID(postId);
                    if (post == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found.");
                        return;
                    }

                    request.setAttribute("post", post);
                    request.getRequestDispatcher("/forum/editpost.jsp").forward(request, response);
                    break;

                case "editSubmit":
                    String title = request.getParameter("title");
                    String bodyText = request.getParameter("bodyText");
                    if (title.isBlank()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Title is required.");
                        return;
                    }
                    if (bodyText.isBlank()){
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Body Text is required.");
                        return;
                    }
                    postDAO.updateTuple(new Element("Posts", "PostID", postId,
                            new String[] { "Title", "BodyText" },
                            new Object[] { title, bodyText}));

                    Post updatedPost = postDAO.getPostByID(postId);
                    session.setAttribute("post", updatedPost);

                    response.sendRedirect("../forum/viewpost.jsp?postId=" + postId);
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
        logger.info("PostActionServlet initialized");
    }

}
