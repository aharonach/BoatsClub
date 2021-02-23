package servlets;

import com.google.gson.Gson;
import data.Notifications;
import server.Response;
import utils.EngineUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "NotificationsServlet", urlPatterns = "/notifications")
public class NotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkPermissions(req);

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

        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }

        // Clear notifications of a user right after he fetches them
        if (!fetchAdminNotification) {
            n.clearNotifications(userId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);
        if (req.getParameter("delete") != null) {
            this.deleteNotification(req, resp);
        } else {
            this.addNotificationToAllUsers(req, resp);
        }
    }

//    protected void addNotification(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, RecordNotFoundException {
//        SessionUtils.checkAdminPermission(req);
//
//        Response response;
//
//        Integer orderId = Integer.parseInt(req.getParameter("orderId"));
//        String message = req.getParameter("message");
//
//        Notifications.addNotificationAuto(orderId, message);
//        response = new Response(true, "Notification to rowers with order ID " + orderId + " sent successfully");
//
//        Gson gson = new Gson();
//        String json = gson.toJson(response);
//
//        try(PrintWriter out = resp.getWriter()) {
//            out.println(json);
//            out.flush();
//        }
//    }

    private void deleteNotification(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;

        EngineUtils.getNotifications(getServletContext()).deleteAdminNotification(Integer.parseInt(req.getParameter("delete")));
        response = new Response(true, "Notification deleted");

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    private void addNotificationToAllUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        String message = req.getParameter("message");

        EngineUtils.getNotifications(getServletContext()).addAdminNotification(message);
        response = new Response(true, "Notification to all Rowers added successfully");

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
