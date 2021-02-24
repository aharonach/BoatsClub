package utils;

import constants.Constants;
import entities.Rower;
import server.Response;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionUtils {

    public static Rower getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (Rower) session.getAttribute(Constants.USERNAME) : null;
    }

    public static void setUser(HttpServletRequest request, Rower rower) {
        HttpSession session = request.getSession(true);
        session.setAttribute(Constants.USERNAME, rower);
    }

    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static boolean checkPermissions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Rower user = getUser(request);
        if (user == null) {
            ServletUtils.sendResponse(response, new Response(false, "You don't have permissions"));
            return false;
        }
        return true;
    }

    public static boolean checkAdminPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Rower user = getUser(request);
        if (user == null || !user.isManager()) {
            ServletUtils.sendResponse(response, new Response(false, "You don't have permissions"));
            return false;
        }
        return true;
    }

    public static void addCookies(Rower rower, HttpServletResponse resp) {
        Cookie c1 = new Cookie("name", rower.getName().split(" ")[0]),
                c2 = new Cookie("isAdmin", rower.isManager() ? "true" : "false");
        resp.addCookie(c1);
        resp.addCookie(c2);
    }

    public static void deleteCookies(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
    }
}