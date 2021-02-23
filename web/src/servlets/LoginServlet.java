package servlets;

import com.google.gson.Gson;
import constants.Constants;
import data.Notifications;
import entities.Rower;
import server.Response;
import utils.EngineUtils;
import utils.SessionUtils;
import utils.TemplateUtils;

import javax.security.auth.login.CredentialNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String logout = req.getParameter("logout");
        if (logout != null && logout.equals("true")) {
            EngineUtils.getNotifications(getServletContext()).clearNotifications(SessionUtils.getUser(req).getId());
            SessionUtils.clearSession(req);
            SessionUtils.deleteCookies(req, resp);
            resp.sendRedirect(Constants.ROOT_PATH);
        }
        TemplateUtils.include("login.html", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        
        resp.setContentType("application/json");

        if (email != null && password != null) {
            String json;
            Gson gson = new Gson();
            try (PrintWriter out = resp.getWriter()) {
                try {
                    Rower rower = EngineUtils.getEngine(getServletContext()).authenticate(email, password);
                    SessionUtils.setUser(req, rower);
                    json = gson.toJson(new Response(true, "Successfully logged in"));
                    SessionUtils.addCookies(rower, resp);
                } catch (CredentialNotFoundException e) {
                    json = gson.toJson(new Response(false, "Credential not found"));
                }
                out.println(json);
                out.flush();
            }
        }
    }
}
