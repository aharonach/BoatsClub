package servlets;

import com.google.gson.Gson;
import entities.Boat;
import entities.Rower;
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

@WebServlet(name = "RowersServlet", urlPatterns = {"/rowers", "/rowers/add", "/rowers/delete", "/rowers/edit"})
public class RowersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtils.checkPermissions(req);

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
            Rower[] boats = EngineUtils.getRowers(getServletContext()).getList();
            json = gson.toJson(boats);
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}