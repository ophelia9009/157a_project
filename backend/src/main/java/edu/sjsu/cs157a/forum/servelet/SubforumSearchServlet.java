package edu.sjsu.cs157a.forum.servelet;

import edu.sjsu.cs157a.forum.dao.SubforumDAO;
import edu.sjsu.cs157a.forum.model.Subforum;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/api/subforumsearch")
public class SubforumSearchServlet extends BaseServlet {
    private final SubforumDAO subforumDAO = new SubforumDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String filterName = request.getParameter("filterName");
            Timestamp minCreationDate = parseTimestamp(request.getParameter("startDate"));
            Timestamp maxCreationDate = parseTimestamp(request.getParameter("endDate"));
            BigInteger minSubscribers = parseBigInteger(request.getParameter("minSubscribers"));
            BigInteger maxSubscribers = parseBigInteger(request.getParameter("maxSubscribers"));
            Timestamp minLastUpdated = parseTimestamp(request.getParameter("lastUpdatedStart"));
            Timestamp maxLastUpdated = parseTimestamp(request.getParameter("lastUpdatedEnd"));

            List<Subforum> subforums = subforumDAO.getFilteredSubforums(
                    filterName, minCreationDate, maxCreationDate, minSubscribers, maxSubscribers, minLastUpdated, maxLastUpdated
            );

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(subforums));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to fetch subforums\"}");
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
