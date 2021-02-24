package servlets;

import com.google.gson.Gson;
import controllers.Boats;
import entities.Boat;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import server.Response;
import utils.EngineUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import wrappers.BoatWrapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "BoatsServlet", urlPatterns = {"/boats", "/boats/edit", "/boats/add", "/boats/delete"})
public class BoatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!SessionUtils.checkAdminPermission(req, resp)){
            return;
        }

        Boats controller = EngineUtils.getBoats(getServletContext());

        Gson gson = new Gson();
        String json = "{}";
        String id = req.getParameter("id");

        if (id == null) {
            Boat[] boats = handleFilter(req);
            json = gson.toJson(boats);
        } else {
            try {
                Boat boat = controller.getRecord(Integer.parseInt(id));
                json = gson.toJson(boat);
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        }

        ServletUtils.sendResponse(resp, json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!SessionUtils.checkAdminPermission(req, resp)){
            return;
        }

        String servletPath = req.getServletPath();

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

    protected void addBoat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Response response;
        Boats controller = EngineUtils.getBoats(getServletContext());

        try {
            Integer id = controller.add(getParams(0, req));
            response = new Response(true, "Boat with ID " + id + " added successfully");
        } catch (RecordAlreadyExistsException e) {
            response = new Response(false, e.getMessage());
        }

        ServletUtils.sendResponse(resp, response);
    }

    protected void editBoat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Response response;
        Boats controller = EngineUtils.getBoats(getServletContext());

        int id = Integer.parseInt(req.getParameter("id"));

        try {
            controller.update(id, getParams(id, req));
            response = new Response(true, "Boat updated successfully");
        } catch (InvalidInputException | RecordNotFoundException | RecordAlreadyExistsException e) {
            response = new Response(false, e.getMessage());
        }

        ServletUtils.sendResponse(resp, response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!SessionUtils.checkAdminPermission(req, resp)){
            return;
        }

        String servletPath = req.getServletPath();

        if (servletPath.equals("/boats/delete")) {
            Response response;
            String id = req.getParameter("id");
            try {
                EngineUtils.getBoats(getServletContext()).delete(Integer.parseInt(id));
                response = new Response(true, "Boat with ID " + id + " deleted with all the orders it was in.");
            } catch (RecordNotFoundException e) {
                response = new Response(false, e.getMessage());
            }

            ServletUtils.sendResponse(resp, response);
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

    protected Boat[] handleFilter(HttpServletRequest req) {
        Boats controller = EngineUtils.getBoats(getServletContext());
        String filter = req.getParameter("filterBy");
        Boat[] boats;
        switch (filter == null ? "" : filter) {
            case "type": // /boats?filterBy=types&types=Single&types=Double
                String type = req.getParameter("type");
                boats = controller.findBoatsByType(Boat.Type.valueOf(type));
                break;
            case "types":
                String types = req.getParameter("types");
                String[] typesArray = types.split(",");
                List<Boat.Type> boatTypes = convertToBoatTypeList(typesArray);
                boats = controller.findBoatsByTypes(boatTypes);
                break;
            case "non-private-non-disabled":
                boats = controller.findBoatByNonPrivateNonDisabled();
                break;
            case "":
            default:
                boats = controller.getList();
                break;
        }
        return boats;
    }

    public static List<Boat.Type> convertToBoatTypeList(String[] types) {
        return Arrays.stream(types).map(Boat.Type::valueOf).collect(Collectors.toList());
    }
}
