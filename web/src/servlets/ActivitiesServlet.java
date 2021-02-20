package servlets;

import com.google.gson.Gson;
import controllers.Activities;
import entities.Activity;
import entities.Boat;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import server.Response;
import utils.EngineUtils;
import utils.SessionUtils;
import wrappers.ActivityWrapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;


@WebServlet(name = "ActivitiesServlet", urlPatterns = {"/activities", "/activities/edit", "/activities/add", "/activities/delete"})
public class ActivitiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        String servletPath = req.getServletPath();

        System.out.println(servletPath + " from get");

        Gson gson = new Gson();
        String json = "{}";

        String id = req.getParameter("id");

        if (id != null) {
            try {
                Activity activity = EngineUtils.getActivites(getServletContext()).getRecord(Integer.parseInt(id));
                json = gson.toJson(activity);
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Activity[] activities = EngineUtils.getActivites(getServletContext()).getList();
            json = gson.toJson(activities);
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        String servletPath = req.getServletPath();

        System.out.println(servletPath + " from post");

        switch (servletPath) {
            case "/activities/add":
                this.addActivity(req, resp);
                break;
            case "/activities/edit":
                this.editActivity(req, resp);
                break;
            case "/activities/delete":
                this.doDelete(req, resp);
                break;
        }
    }

    protected void addActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        Activities controller = EngineUtils.getActivites(getServletContext());

        try {
            Integer id = controller.add(getParams(0, req));
            response = new Response(true, "Activity with ID " + id + " added successfully");
        } catch (RecordAlreadyExistsException | InvalidInputException e) {
            response = new Response(false, e.getMessage());
        }

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    protected void editActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        Activities controller = EngineUtils.getActivites(getServletContext());

        int id = Integer.parseInt(req.getParameter("id"));

        try {
            controller.update(id, getParams(id, req));
            response = new Response(true, "Activity updated successfully");
        } catch (InvalidInputException | RecordNotFoundException | RecordAlreadyExistsException e) {
            response = new Response(false, e.getMessage());
        }

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        String servletPath = req.getServletPath();

        if (servletPath.equals("/activities/delete")) {
            Response response;
            String id = req.getParameter("id");
            try {
                EngineUtils.getActivites(getServletContext()).delete(Integer.parseInt(id));
                response = new Response(true, "Activity with ID " + id + " deleted successfully.");
            } catch (RecordNotFoundException e) {
                response = new Response(false, e.getMessage());
            }
            Gson gson = new Gson();
            String json = gson.toJson(response);

            try(PrintWriter out = resp.getWriter()) {
                out.println(json);
                out.flush();
            }
        }
    }

    private ActivityWrapper getParams(int id, HttpServletRequest req) {
        String title = req.getParameter("title");

        System.out.println("START TIME: " + req.getParameter("startTime")); //debugging
        System.out.println("END TIME: " + req.getParameter("endTime")); //debugging

        LocalTime startTime = LocalTime.parse(req.getParameter("startTime"));
        LocalTime endTime = LocalTime.parse(req.getParameter("endTime"));
        Boat.Type type = null;
//        System.out.println(req.getParameter("type"));
//        System.out.println(!req.getParameter("type").equalsIgnoreCase("-1"));
        if( req.getParameter("boatType") != null )
            if (!req.getParameter("boatType").equalsIgnoreCase("-1"))
                type = Boat.Type.valueOf(req.getParameter("boatType"));

        return new ActivityWrapper(title, startTime, endTime, type);
    }
}