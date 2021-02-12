package servlets;

import constants.Constants;
import entities.Rower;
import utils.EngineUtils;
import utils.SessionUtils;
import utils.TemplateUtils;

import javax.security.auth.login.CredentialNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String logout = req.getParameter("logout");
        if (logout != null && logout.equals("true")) {
            SessionUtils.clearSession(req);
            resp.sendRedirect(Constants.ROOT_PATH);
        }
        TemplateUtils.include("login.html", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email != null && password != null) {
            try {
                Rower rower = EngineUtils.getEngine(getServletContext()).authenticate(email, password);
                SessionUtils.setUser(req, rower);
                resp.sendRedirect(Constants.ROOT_PATH);
            } catch (CredentialNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
