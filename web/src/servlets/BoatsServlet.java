package servlets;

import com.google.gson.Gson;
import controllers.Boats;
import entities.Boat;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import server.Response;
import utils.EngineUtils;
import utils.SessionUtils;
import wrappers.BoatWrapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "BoatsServlet", urlPatterns = {"/boats", "/boats/edit", "/boats/add", "/boats/delete"})
public class BoatsServlet extends HttpServlet {
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
                Boat boat = EngineUtils.getBoats(getServletContext()).getRecord(Integer.parseInt(id));
                json = gson.toJson(boat);
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Boat[] boats = EngineUtils.getBoats(getServletContext()).getList();
            json = gson.toJson(boats);
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
            case "/boats/add":
                this.addBoat(req, resp);
                break;
            case "/boats/edit":
                this.editBoat(req, resp);
                break;
            case "/boats/delete":
                this.doDelete(req, resp);
                break;
        }
    }

    protected void addBoat(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        Boats controller = EngineUtils.getBoats(getServletContext());

        try {
            Integer id = controller.add(getParams(0, req));
            response = new Response(true, "Boat with ID " + id + " added successfully");
        } catch (RecordAlreadyExistsException e) {
            response = new Response(false, e.getMessage());
        }

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    protected void editBoat(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        Boats controller = EngineUtils.getBoats(getServletContext());

        int id = Integer.parseInt(req.getParameter("id"));

        try {
            controller.update(id, getParams(id, req));
            response = new Response(true, "Boat updated successfully");
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

        if (!servletPath.equals("/boats/delete")) {
            String id = req.getParameter("id");
            try {
                EngineUtils.getBoats(getServletContext()).delete(Integer.parseInt(id));
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private BoatWrapper getParams(int id, HttpServletRequest req) {
        String name = req.getParameter("name");
        Boat.Type type = Boat.Type.valueOf(req.getParameter("type"));
        Boolean isPrivate = Boolean.parseBoolean(req.getParameter("isPrivate"));
        Boolean isWide = Boolean.parseBoolean(req.getParameter("isWide"));
        Boolean isCoastal = Boolean.parseBoolean(req.getParameter("isCoastal"));
        Boolean isDisabled = Boolean.parseBoolean(req.getParameter("isDisabled"));

        return new BoatWrapper(id, name, type, isPrivate, isWide, isCoastal, isDisabled);
    }
}
