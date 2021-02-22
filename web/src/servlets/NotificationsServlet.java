package servlets;

import com.google.gson.Gson;
import data.Notifications;
import exceptions.RecordNotFoundException;
import server.Response;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "NotificationsServlet", urlPatterns = "/notifications")
public class NotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkPermissions(req);

        Gson gson = new Gson();
        String json = gson.toJson(Notifications.getUserNotifications(SessionUtils.getUser(req).getId()));

        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);
        this.addNotificationToAllUsers(req, resp);
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

    private void addNotificationToAllUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        String message = req.getParameter("message");

        Notifications.addNotificationToAllUsers(message);
        response = new Response(true, "Notification to all Rowers added successfully");

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
