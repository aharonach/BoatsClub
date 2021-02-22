package servlets;

import com.google.gson.Gson;
import controllers.Boats;
import data.Notifications;
import entities.Boat;
import exceptions.RecordNotFoundException;
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

        Gson gson = new Gson();
        String json = "{}";
        String id = req.getParameter("id");

        final Map<Integer, List<String>> notifications = Notifications.getNotifications();
        json = gson.toJson(notifications);


        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        SessionUtils.checkPermissions(req);
//
//        String servletPath = req.getServletPath();
//
//        switch (servletPath) {
//            case "/notifications/auto":
//                this.addNotificationForAll(req, resp);
//                break;
//            case "/orders/edit":
//                this.addNotificationToUser(req, resp);
//                break;
//        }
//    }


}
