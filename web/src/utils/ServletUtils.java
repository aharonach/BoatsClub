package utils;

import com.google.gson.Gson;
import server.Response;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletUtils {
    public static void include(String htmlFilePath, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        RequestDispatcher rd = req.getRequestDispatcher(htmlFilePath);
        rd.include(req, resp);
    }

    public static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();

        String baseUrl = scheme + "://" + host + ((("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) ? "" : ":" + port) + contextPath;
        return baseUrl;
    }

    public static void sendResponse(HttpServletResponse servletResponse, Object resp) throws IOException {
        Gson gson = new Gson();
        try(PrintWriter out = servletResponse.getWriter()) {
            out.println(resp instanceof String ? resp : gson.toJson(resp));
            out.flush();
        }
    }
}
