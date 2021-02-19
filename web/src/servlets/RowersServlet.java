package servlets;

import com.google.gson.Gson;
import controllers.Rowers;
import entities.Rower;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import server.Response;
import utils.EngineUtils;
import utils.SessionUtils;
import wrappers.RowerWrapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import static entities.Rower.yearsUntilExpired;

@WebServlet(name = "RowersServlet", urlPatterns = {"/rowers", "/rowers/edit", "/rowers/add", "/rowers/delete"})
public class RowersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        Gson gson = new Gson();
        String json = "{}";

        String id = req.getParameter("id");

        if (id != null) {
            try {
                Rower rower = EngineUtils.getRowers(getServletContext()).getRecord(Integer.parseInt(id));
                json = gson.toJson(rower);
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Rower[] rowers = EngineUtils.getRowers(getServletContext()).getList();
            json = gson.toJson(rowers);
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

        switch (servletPath) {
            case "/rowers/add":
                this.addRower(req, resp);
                break;
            case "/rowers/edit":
                this.editRower(req, resp);
                break;
            case "/rowers/delete":
                this.doDelete(req, resp);
                break;
        }
    }

    protected void addRower(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        Rowers controller = EngineUtils.getRowers(getServletContext());

        try {
            Integer id = controller.add(getParams(0, req));
            response = new Response(true, "Rower with ID " + id + " added successfully");
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

    protected void editRower(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response;
        Rowers controller = EngineUtils.getRowers(getServletContext());

        int id = Integer.parseInt(req.getParameter("id"));

        try {
            controller.update(id, getParams(id, req));
            response = new Response(true, "Rower updated successfully");
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

        if (servletPath.equals("/rowers/delete")) {
            Response response;
            String id = req.getParameter("id");
            try {
                EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
                EngineUtils.getRowers(getServletContext()).delete(Integer.parseInt(id));
                response = new Response(true, "Rower with ID " + id + " deleted. He was deleted also from his orders.");
                EngineUtils.getEngine(getServletContext()).setUser(null);
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

    private RowerWrapper getParams(int id, HttpServletRequest req) {
        String name = req.getParameter("name");
        Integer age = Integer.parseInt(req.getParameter("age"));
        Rower.Level level = Rower.Level.valueOf(req.getParameter("level"));
        Boolean hasPrivateBoat = Boolean.parseBoolean(req.getParameter("hasPrivateBoat"));
        Integer privateBoat = null;
        if (hasPrivateBoat && req.getParameter("privateBoat") != null) {
            privateBoat = Integer.parseInt(req.getParameter("privateBoat"));
        }
        String phoneNumber = req.getParameter("phoneNumber");
        String notes = req.getParameter("notes");
        Boolean isManager = Boolean.parseBoolean(req.getParameter("isManager"));
        String emailAddress = req.getParameter("emailAddress");
        String password = req.getParameter("password");

        return new RowerWrapper(id, name, age, LocalDateTime.now(), level, hasPrivateBoat, privateBoat, phoneNumber,
                notes, isManager, emailAddress, password, LocalDateTime.now().plusYears(yearsUntilExpired));
    }
}