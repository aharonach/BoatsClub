package servlets;

import entities.Rower;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MainServlet", urlPatterns = "") // root path
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Rower rower = SessionUtils.getUser(req);
        if (rower == null) {
            resp.sendRedirect("login");
        } else if (rower.isManager()) {
            ServletUtils.include("adminMenu.html", req, resp);
        } else {
            ServletUtils.include("userMenu.html", req, resp);
        }
    }
}
