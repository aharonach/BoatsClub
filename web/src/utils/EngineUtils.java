package utils;

import controllers.Activities;
import controllers.Boats;
import controllers.Orders;
import controllers.Rowers;
import data.Notifications;
import engine.BCEngine;
import interfaces.Controller;

import javax.servlet.ServletContext;

public class EngineUtils {
    public static BCEngine getEngine(ServletContext context) {
        return (BCEngine) context.getAttribute("engine");
    }

    private static Controller getController(ServletContext context, String entityType) {
        return getEngine(context).getController(entityType);
    }

    public static Notifications getNotifications(ServletContext context) {
        return getEngine(context).getNoticiations();
    }

    public static Rowers getRowers(ServletContext context) {
        return (Rowers) getController(context, "rowers");
    }

    public static Boats getBoats(ServletContext context) {
        return (Boats) getController(context, "boats");
    }

    public static Activities getActivities(ServletContext context) {
        return (Activities) getController(context, "activities");
    }

    public static Orders getOrders(ServletContext context) {
        return (Orders) getController(context, "orders");
    }
}
