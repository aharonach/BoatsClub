package servlets;

import constants.Constants;
import entities.Rower;
import server.Response;
import utils.EngineUtils;
import utils.ServletUtils;
import utils.SessionUtils;

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
            EngineUtils.getNotifications(getServletContext()).clearNotifications(SessionUtils.getUser(req).getId());
            SessionUtils.clearSession(req);
            SessionUtils.deleteCookies(req, resp);
            resp.sendRedirect(Constants.ROOT_PATH);
        }
        ServletUtils.include("login.html", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        
//        resp.setContentType("application/json");

        if (email != null && password != null) {
            Response response;
            try {
                Rower rower = EngineUtils.getEngine(getServletContext()).authenticate(email, password);
                SessionUtils.setUser(req, rower);
                response = new Response(true, "Successfully logged in");
                SessionUtils.addCookies(rower, resp);
            } catch (CredentialNotFoundException e) {
                response = new Response(false, "Wrong email or password");
            }
            ServletUtils.sendResponse(resp, response);
        }
    }
}
