package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifications {

    private final Map<Integer, List<String>> notifications = new HashMap<>();

    public Notifications() {

    }

    public void addNotification(Integer rowerId, String message) {
        List<String> rowerNotific = notifications.get(rowerId);
        if (rowerNotific == null) {
            rowerNotific = new ArrayList<>();
        }
        rowerNotific.add(message);
    }
}
