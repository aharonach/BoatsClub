package utils;

import engine.BCEngine;
import entities.Rower;
import interfaces.*;

import javax.servlet.ServletContext;

public class EngineUtils {
    public static BCEngine getEngine(ServletContext context) {
        return (BCEngine) context.getAttribute("engine");
    }

    private static Controller getController(ServletContext context, String entityType) {
        return getEngine(context).getController(entityType);
    }

    public static RowersController getRowers(ServletContext context) {
        return (RowersController) getController(context, "rowers");
    }

    public static BoatsController getBoats(ServletContext context) {
        return (BoatsController) getController(context, "boats");
    }

    public static ActivitiesController getActivites(ServletContext context) {
        return (ActivitiesController) getController(context, "activities");
    }

    public static OrdersController getOrders(ServletContext context) {
        return (OrdersController) getController(context, "orders");
    }

    public static Rower getCurrentUser(ServletContext context) {
        return (Rower) context.getAttribute("user");
    }
}
