package servlets;

import com.google.gson.Gson;
import engine.BCEngine;
import server.Response;
import utils.EngineUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@WebServlet(name ="ImportServlet", urlPatterns = "/import")
@MultipartConfig
public class ImportServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!SessionUtils.checkAdminPermission(req, resp)){
            return;
        }

        BCEngine engine = EngineUtils.getEngine(getServletContext());

        String entity = req.getParameter("entity");
        boolean override = Boolean.parseBoolean(req.getParameter("override-records"));

        Part filePart = req.getPart("uploadFile");
        InputStream inputStream = filePart.getInputStream();
        String fileContent;

        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            fileContent = scanner.useDelimiter("\\A").next();
            List<String> feedback;
            feedback = engine.importRecords(entity, fileContent, override);
            Gson gson = new Gson();
            Response response = new Response(null, feedback);
            try(PrintWriter out = resp.getWriter()) {
                out.println(gson.toJson(response));
                out.flush();
            }
        }
    }
}

