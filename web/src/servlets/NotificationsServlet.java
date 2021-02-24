package servlets;

import com.google.gson.Gson;
import data.Notifications;
import server.Response;
import utils.EngineUtils;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NotificationsServlet", urlPatterns = "/notifications")
public class NotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!SessionUtils.checkPermissions(req, resp)){
            return;
        }

        boolean fetchAdminNotification = req.getParameter("admin") != null;
        int userId = SessionUtils.getUser(req).getId();

        Notifications n = EngineUtils.getNotifications(getServletContext());
        Gson gson = new Gson();
        String json;

        if (fetchAdminNotification) {
            json = gson.toJson(n.getAdminNotifications());
        } else {
            json = gson.toJson(n.getUserNotifications(userId));
        }

        ServletUtils.sendResponse(resp, json);

        // Clear notifications of a user right after he fetches them
        if (!fetchAdminNotification) {
            n.clearNotifications(userId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!SessionUtils.checkAdminPermission(req, resp)){
            return;
        }
        if (req.getParameter("delete") != null) {
            this.deleteNotification(req, resp);
        } else {
            this.addNotificationToAllUsers(req, resp);
        }
    }

    private void deleteNotification(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Response response;

        EngineUtils.getNotifications(getServletContext()).deleteAdminNotification(Integer.parseInt(req.getParameter("delete")));
        response = new Response(true, "Notification deleted");

        ServletUtils.sendResponse(resp, response);
    }

    private void addNotificationToAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Response response;
        String message = req.getParameter("message");

        EngineUtils.getNotifications(getServletContext()).addAdminNotification(message);
        response = new Response(true, "Notification to all Rowers added successfully");

        ServletUtils.sendResponse(resp, response);
    }
}
