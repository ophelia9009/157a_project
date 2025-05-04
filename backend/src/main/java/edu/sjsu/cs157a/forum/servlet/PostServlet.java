package edu.sjsu.cs157a.forum.servlet;

import com.google.gson.Gson;
import edu.sjsu.cs157a.forum.dao.PostDAO;
import edu.sjsu.cs157a.forum.model.Post;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class PostServlet extends BaseServlet{
    private final PostDAO postDAO = new PostDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String filterName = request.getParameter("filterName");
            Timestamp minCreationDate = parseTimestamp(request.getParameter("startDate"));
            Timestamp maxCreationDate = parseTimestamp(request.getParameter("endDate"));
            BigInteger minSubscribers = parseBigInteger(request.getParameter("minRating"));
            BigInteger maxSubscribers = parseBigInteger(request.getParameter("maxRating"));
            Timestamp minLastUpdated = parseTimestamp(request.getParameter("lastUpdatedStart"));
            Timestamp maxLastUpdated = parseTimestamp(request.getParameter("lastUpdatedEnd"));
            Integer SubforumID = Integer.valueOf(request.getParameter("SubforumID"));

            List<Post> posts = postDAO.getFilteredPosts(
                    filterName, minCreationDate, maxCreationDate, minSubscribers, maxSubscribers, minLastUpdated, maxLastUpdated
            , SubforumID);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(posts));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to fetch posts\"}");
        }
    }

    private Timestamp parseTimestamp(String date) {
        try {
            return date != null && !date.isEmpty() ? Timestamp.valueOf(date + " 00:00:00") : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private BigInteger parseBigInteger(String number) {
        try {
            return number != null && !number.isEmpty() ? new BigInteger(number) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
