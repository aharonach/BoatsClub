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

@WebServlet(name = "OrdersServlet", urlPatterns = {"/orders", "/orders/edit", "/orders/add", "/orders/delete", "/orders/appoint", "/orders/duplicate", "/orders/merge"})
public class OrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkPermissions(req);
        Gson gson = new Gson();
        String json = "{}";
        String id = req.getParameter("id");

        if (id == null) {
            Order[] orders = handleFilter(req);
            json = gson.toJson(orders);
        } else {
            try {
                Order order = EngineUtils.getOrders(getServletContext()).getRecord(Integer.parseInt(id));
                json = gson.toJson(order);
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkPermissions(req);

        String servletPath = req.getServletPath();
        System.out.println(servletPath);

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
            case "/orders/appoint":
                this.appointOrder(req, resp);
                break;
            case "/orders/duplicate":
                try {
                    this.duplicateOrder(req, resp);
                } catch (RecordNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "/orders/merge":
                try {
                    this.mergeOrders(req, resp);
                } catch (RecordNotFoundException e) {
                    e.printStackTrace();
                }
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


    protected void appointOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        Response response;
        Orders controller = EngineUtils.getOrders(getServletContext());

        int id = Integer.parseInt(req.getParameter("id"));
        int boatId = Integer.parseInt(req.getParameter("boat"));

        try {
            EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
            controller.appointBoatToOrder(boatId, id);
            response = new Response(true, "Order with ID " + id + " appointed successfully");
            EngineUtils.getEngine(getServletContext()).setUser(null);
        } catch (InvalidInputException | RecordNotFoundException e) {
            response = new Response(false, e.getMessage());
        }

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    protected void duplicateOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, RecordNotFoundException {
        SessionUtils.checkAdminPermission(req);

        System.out.println("here");

        Response response;
        Orders controller = EngineUtils.getOrders(getServletContext());

        int id = Integer.parseInt(req.getParameter("id"));
        OrderWrapper updatedDuplicate = getParams(id, req);

        try {
            EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
            controller.duplicateOrder(id, updatedDuplicate);
            response = new Response(true, "Order with ID " + id + " duplicated");
            EngineUtils.getEngine(getServletContext()).setUser(null);
        } catch (InvalidInputException | RecordNotFoundException | CloneNotSupportedException e) {
            response = new Response(false, e.getMessage());
        }

        Gson gson = new Gson();
        String json = gson.toJson(response);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    protected void mergeOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, RecordNotFoundException {
        SessionUtils.checkPermissions(req);

        Response response;
        Orders controller = EngineUtils.getOrders(getServletContext());

        System.out.println("order 1: " +Integer.parseInt(req.getParameter("order1")));
        System.out.println("order 2: " +Integer.parseInt(req.getParameter("order2")));

        OrderWrapper orderToKeep = controller.get(Integer.parseInt(req.getParameter("order1")));
        OrderWrapper orderToMerge = controller.get(Integer.parseInt(req.getParameter("order2")));

        try {
            EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
            controller.merge(orderToKeep, orderToMerge);
            response = new Response(true, "Order with id: " + orderToMerge.getId() + " merged into order with id: " + orderToKeep.getId() + " successfully.");
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

        LocalDate activityDate = LocalDate.parse(req.getParameter("activityDate"));
        String activityTitle = req.getParameter("activityTitle");
        LocalTime activityStartTime = LocalTime.parse(req.getParameter("activityStartTime"));
        LocalTime activityEndTime = LocalTime.parse(req.getParameter("activityEndTime"));

        return new OrderWrapper(id, rowers, activityTitle, activityDate, activityStartTime, activityEndTime,
                boatTypes, false, null);
    }

    protected Order[] handleFilter(HttpServletRequest req) {
        Orders controller = EngineUtils.getOrders(getServletContext());
        String filter = req.getParameter("filterBy");
        System.out.println(filter);
        controller.engine().setUser(SessionUtils.getUser(req).getId());
        Order[] orders;
        switch (filter == null ? "" : filter) {
            case "user":
                orders = controller.findOrdersCreatedByRower(SessionUtils.getUser(req).getId());
                break;
            case "appointed":
                orders = controller.filterList(o -> ((Order) o).isApprovedRequest());
                break;
            case "non-appointed":
                orders = controller.filterList(o -> !((Order) o).isApprovedRequest());
                break;
            case "today":
                orders = controller.findOrdersByDate(LocalDate.now());
                break;
            case "lastWeek":
                orders = controller.findOrdersFromLastWeek();
                break;
            case "date":
                LocalDate date = LocalDate.parse(req.getParameter("date"));
                orders = controller.findOrdersByDate(date);
                break;
            case "week":
                LocalDate fromDate = LocalDate.parse(req.getParameter("date"));
                orders = controller.findOrdersFromDateToDate(fromDate, fromDate.plusWeeks(1));
                break;
            case "":
            default:
                orders = controller.getList();
                break;
        }
        controller.engine().setUser(null);
        return orders;
    }
}
