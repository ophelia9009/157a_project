package com.example.servelet;

import com.example.dao.BaseDAO;
import com.example.dao.UserDAO;
import com.example.model.User;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
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
}