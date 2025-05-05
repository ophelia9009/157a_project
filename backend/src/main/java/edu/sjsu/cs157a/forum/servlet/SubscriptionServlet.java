package edu.sjsu.cs157a.forum.servlet;

import java.io.IOException;

import edu.sjsu.cs157a.forum.dao.SubscriptionDAO;
import edu.sjsu.cs157a.forum.dao.UserDAO;
import edu.sjsu.cs157a.forum.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/subscription")
public class SubscriptionServlet extends HttpServlet {


    private static final Logger logger = LogManager.getLogger(UserDAO.class);


    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String subforumIdParam = request.getParameter("subforumId");
            if (subforumIdParam == null || subforumIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing subforumId parameter");
                return;
            }
            
            Integer subforumId = Integer.parseInt(subforumIdParam);
            User user = (User) request.getSession().getAttribute("user");
            
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
                return;
            }
            
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
            boolean isSubscribed = subscriptionDAO.isUserSubscribed(user.getUserID(), subforumId);
            
            response.setContentType("application/json");
            response.getWriter().write("{\"isSubscribed\": " + isSubscribed + "}");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subforumId format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error checking subscription status");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {



        logger.info("Received POST request for subscription status");

        try {
            String subforumIdParam = request.getParameter("subforumId");


            logger.info("Received POST request for subscription status, subforumIdParam: " + subforumIdParam);
            String action = request.getParameter("action");

            logger.info("Received POST request for subscription status, action: " + action);
            
            if (subforumIdParam == null || subforumIdParam.isEmpty() || action == null) {
                logger.warn("Missing required parameters: subforumId or action");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }
            
            Integer subforumId = Integer.parseInt(subforumIdParam);
            Integer userId = ((User) request.getSession().getAttribute("user")).getUserID();
            
            if (userId == null) {
                logger.warn("User not logged in, userId is null");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
                return;
            }
            
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
            boolean success = false;
            
            if ("subscribe".equals(action)) {
                logger.info("Subscribing userId: " + userId + " to subforumId: " + subforumId);
                success = subscriptionDAO.subscribeUserToSubforum(userId, subforumId);
            } else if ("unsubscribe".equals(action)) {
                logger.info("Unsubscribing userId: " + userId + " from subforumId: " + subforumId);
                success = subscriptionDAO.unsubscribeUserFromSubforum(userId, subforumId);
            } else {
                logger.warn("Invalid action: " + action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
            }
            
            if (success) {
                logger.info("Subscription action successful, userId: " + userId + ", subforumId: " + subforumId);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true}");
            } else {
                logger.warn("Subscription action failed, userId: " + userId + ", subforumId: " + subforumId);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subscription action failed");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid subforumId format", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subforumId format");
        } catch (Exception e) {
            logger.error("Error processing subscription", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing subscription");
        }
    }
}