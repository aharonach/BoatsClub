package utils;

import controllers.Activities;
import controllers.Boats;
import controllers.Orders;
import controllers.Rowers;
import data.Notifications;
import engine.BCEngine;
import interfaces.*;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

public class EngineUtils {
    public static BCEngine getEngine(ServletContext context) {
        return (BCEngine) context.getAttribute("engine");
    }

    private static Controller getController(ServletContext context, String entityType) {
        return getEngine(context).getController(entityType);
    }

    public static Map<Integer, List<String>> getNotifications(ServletContext context) {
        return Notifications.getAllNotifications();
    }

    public static Rowers getRowers(ServletContext context) {
        return (Rowers) getController(context, "rowers");
    }

    public static Boats getBoats(ServletContext context) {
        return (Boats) getController(context, "boats");
    }

    public static Activities getActivites(ServletContext context) { return (Activities) getController(context, "activities"); }

    public static Orders getOrders(ServletContext context) {
        return (Orders) getController(context, "orders");
    }
}
