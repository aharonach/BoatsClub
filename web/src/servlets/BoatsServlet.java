package servlets;

import com.google.gson.Gson;
import entities.Boat;
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

@WebServlet(name = "BoatsServlet", urlPatterns = "/boats")
public class BoatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkAdminPermission(req);

        Boat[] boats = EngineUtils.getBoats(getServletContext()).getList();
        Gson gson = new Gson();
        String json = gson.toJson(boats);

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
