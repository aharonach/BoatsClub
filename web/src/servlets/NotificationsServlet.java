package servlets;

import com.google.gson.Gson;
import controllers.Boats;
import controllers.Orders;
import data.Notifications;
import entities.Boat;
import entities.Order;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
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
import java.util.List;
import java.util.Map;

@WebServlet(name = "NotificationsServlet", urlPatterns = {"/notifications", "/notifications/auto", "/notifications/manual"})
public class NotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        String id = req.getParameter("id");
        Gson gson = new Gson();
        Map<Integer, List<String>> notifications;
        List<String> userNotifications;
        String json = "{}";

        if (id == null) {
            notifications = Notifications.getAllNotifications();
            json = gson.toJson(notifications);
        } else {
            userNotifications = Notifications.getUserNotifications(Integer.parseInt(id));
            json = gson.toJson(userNotifications);
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        String servletPath = req.getServletPath();
        System.out.println(servletPath);

        switch (servletPath) {
            case "/notifications/auto":
                try {
                    this.addNotification(req, resp);
                } catch (RecordNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "/notifications/manual":
                this.addNotificationToAllUsers(req, resp);
                break;
        }
    }

    protected void addNotification(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, RecordNotFoundException {
        SessionUtils.checkAdminPermission(req);

        Response response;

        Integer orderId = Integer.parseInt(req.getParameter("orderId"));
        String message = req.getParameter("message");

        Notifications.addNotificationAuto(orderId, message);
        response = new Response(true, "Notification to rowers with order ID " + orderId + " sent successfully");

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    protected void addNotificationToAllUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

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
