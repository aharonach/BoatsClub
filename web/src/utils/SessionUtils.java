package utils;

import constants.Constants;
import entities.Rower;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    public static boolean checkPermissions(HttpServletRequest request) {
        Rower user = getUser(request);
        return user != null;
    }

    public static void checkAdminPermission(HttpServletRequest request) throws ServletException {
        Rower user = getUser(request);
        if (user != null && !user.isManager()) {
            throw new ServletException("You don't have permissions");
        }
    }
}