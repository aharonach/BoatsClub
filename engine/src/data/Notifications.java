package data;

import engine.BCEngine;
import entities.Order;
import entities.Rower;
import exceptions.RecordNotFoundException;
import server.Message;

import java.util.*;

public class Notifications {

    private final Map<Integer, List<Message>> notifications;
    private final List<Message> adminNotifications;

    public Notifications(Rower[] rowers) { //initialize rowers
        notifications = new HashMap<>();
        adminNotifications = new ArrayList<>();
        for (Rower rower: rowers) {
            notifications.put(rower.getId(), new ArrayList<>());
        }
    }

    public void addUser(Integer rowerId) {
        notifications.put(rowerId, new ArrayList<>());
    }

    public void removeUser(Integer rowerId){
        notifications.remove(rowerId);
    }

    public void addAutoMessage(Integer orderId, String content, Set<Integer> rowerIds) {
        Message message = new Message(false, content, orderId);

        for(Integer rowerId : rowerIds){
            List<Message> rowerNotific = notifications.get(rowerId);
            if (rowerNotific == null)
                rowerNotific = new ArrayList<>();
            rowerNotific.add(message);
            notifications.put(rowerId, rowerNotific);
        }
    }

    public void addAdminNotification(String message) {
        adminNotifications.add(new Message(true, message, null));
    }

    public void deleteAdminNotification(int messageIndex) {
        adminNotifications.remove(messageIndex);
    }

    public void clearAdminNotifications() {
        adminNotifications.clear();
    }

    public void clearNotifications(int rowerId) {
        notifications.remove(rowerId);
    }

    public List<Message> getAdminNotifications() {
        return adminNotifications;
    }

    public List<Message> getUserNotifications(Integer rowerId) {
        return notifications.get(rowerId);
    }
}
