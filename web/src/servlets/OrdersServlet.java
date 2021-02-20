package servlets;

import com.google.gson.Gson;
import controllers.Orders;
import entities.Boat;
import entities.Order;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import server.Response;
import utils.EngineUtils;
import utils.SessionUtils;
import wrappers.ActivityWrapper;
import wrappers.OrderWrapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrdersServlet", urlPatterns = {"/orders", "/orders/edit", "/orders/add", "/orders/delete"})
public class OrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        Gson gson = new Gson();
        String json = "{}";

        String id = req.getParameter("id");

        if (id != null) {
            try {
                Order order = EngineUtils.getOrders(getServletContext()).getRecord(Integer.parseInt(id));
                json = gson.toJson(order);
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Order[] orders = EngineUtils.getOrders(getServletContext()).getList();
            json = gson.toJson(orders);
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
            case "/orders/add":
                this.addOrder(req, resp);
                break;
            case "/orders/edit":
                this.editOrder(req, resp);
                break;
            case "/orders/delete":
                this.doDelete(req, resp);
                break;
        }
    }

    protected void addOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkPermissions(req);

        Response response;
        Orders controller = EngineUtils.getOrders(getServletContext());

        try {
            EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
            Integer id = controller.add(getParams(0, req));
            response = new Response(true, "Order with ID " + id + " added successfully");
            EngineUtils.getEngine(getServletContext()).setUser(null);
        } catch (RecordAlreadyExistsException | InvalidInputException | RecordNotFoundException e) {
            response = new Response(false, e.getMessage());
        }

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    protected void editOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkPermissions(req);
        Response response;
        Orders controller = EngineUtils.getOrders(getServletContext());

        int id = Integer.parseInt(req.getParameter("id"));

        try {
            EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
            controller.update(id, getParams(id, req));
            response = new Response(true, "Order updated successfully");
            EngineUtils.getEngine(getServletContext()).setUser(null);
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

        if (servletPath.equals("/orders/delete")) {
            Response response;
            String id = req.getParameter("id");
            try {
                EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
                EngineUtils.getOrders(getServletContext()).delete(Integer.parseInt(id));
                response = new Response(true, "Order with ID " + id + " deleted. He was deleted also from his orders.");
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

    private OrderWrapper getParams(int id, HttpServletRequest req) throws RecordNotFoundException {
        List<Boat.Type> boatTypes = new ArrayList<>();
        for(String boatTypeString: req.getParameterValues("boatTypes")) {
            System.out.println(Boat.Type.valueOf(boatTypeString).getMaxCapacity());
            boatTypes.add((Boat.Type.valueOf(boatTypeString)));
        }

        List<Integer> rowers = new ArrayList<>();
        for(String rowerString: req.getParameterValues("rowers")) {
            rowers.add(Integer.parseInt(rowerString));
        }

        ActivityWrapper wantedActivity = EngineUtils.getActivites(getServletContext()).get(Integer.parseInt(req.getParameter("wantedActivity")));
        String activityTitle = wantedActivity.getTitle();
        LocalTime activityStartTime = wantedActivity.getStartTime();
        LocalTime activityEndTime = wantedActivity.getEndTime();

        LocalDate activityDate = LocalDate.parse(req.getParameter("activityDate"));

        return new OrderWrapper(id, rowers, activityTitle, activityDate, activityStartTime, activityEndTime,
                boatTypes, false, null);
    }
}

