package servlets;

import engine.BCEngine;
import utils.EngineUtils;
import utils.SessionUtils;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "ExportServlet", urlPatterns = "/export")
public class ExportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!SessionUtils.checkAdminPermission(req, resp)){
            return;
        }

        BCEngine engine = EngineUtils.getEngine(getServletContext());

        String entity = req.getParameter("entity");

        File file = engine.exportRecordsToFile(entity, getServletContext().getRealPath("/"));

        resp.sendRedirect(ServletUtils.getBaseUrl(req).concat("/" + file.getName()));
    }
}
