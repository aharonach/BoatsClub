package data;

import controllers.Entities;
import controllers.Orders;
import controllers.Rowers;
import engine.BCEngine;
import entities.Order;
import entities.Rower;
import exceptions.RecordNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifications {

    private static final Map<Integer, List<String>> notifications = new HashMap<>();

    public Notifications(Rower[] rowers) { //initialize rowers
        for (Rower rower: rowers){
            List<String> messages = new ArrayList<>();
            notifications.put(rower.getId(), messages);
        }
    }

    public static void addUser(Integer rowerId) {
        List<String> messages = new ArrayList<>();
        messages.add("");
        notifications.put(rowerId, messages);
    }

    public static void removeUser(Integer rowerId){
        notifications.remove(rowerId);
    }

    public static void addNotificationAuto(Integer orderId, String message) throws RecordNotFoundException {
        Order order = (Order) BCEngine.instance().getRecord("orders", orderId);
        List<Integer> orderRowers = order.getRowers();

        for(Integer rowerId: orderRowers){
            List<String> rowerNotific = notifications.get(rowerId);
            rowerNotific.add(message);
        }

        notifications.get(order.getRegisterRower()).add(message);

        System.out.println("Adding notification: " + message);
        System.out.println(notifications.get(1));
    }

    public static void addNotificationToAllUsers(String message) {
        notifications.forEach((k, v) -> v.add(message));
    }

    public static void clearNotifications() {
        notifications.clear();
    }

    public static void clearNotifications(int rowerId) {
        notifications.remove(rowerId);
    }

    public static Map<Integer, List<String>> getAllNotifications() {
        return notifications;
    }

    public static List<String> getUserNotifications(Integer rowerId) {
        System.out.println(notifications.get(rowerId));
        return notifications.get(rowerId);
    }
}
