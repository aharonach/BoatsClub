package servlets;

import com.google.gson.Gson;
import entities.Order;
import utils.EngineUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@WebServlet(name = "OrdersServlet", urlPatterns = "/orders")
public class OrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        if(SessionUtils.getUser(req) != null) {
            EngineUtils.getEngine(getServletContext()).setUser(SessionUtils.getUser(req).getId());
        }

        Order[] orders = EngineUtils.getOrders(getServletContext()).getList();

        System.out.println(Arrays.toString(orders));
        Gson gson = new Gson();
        String json = gson.toJson(orders);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }

        EngineUtils.getEngine(getServletContext()).setUser(null);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}

