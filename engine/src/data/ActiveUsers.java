package data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class ActiveUsers {
    private static final Integer TIMEOUT_IN_MINUTES = 30;
    private static final Map<Integer, LocalDateTime> activeUsersId = new HashMap<>();

    public static void addActiveUser(int userId) {
        activeUsersId.put(userId, LocalDateTime.now());
    }

    public static void removeActiveUser(int userId) {
        activeUsersId.remove(userId);
    }

    public static boolean isAlreadyActive(int userId) {
        return activeUsersId.containsKey(userId);
    }

    public static int getTimeoutOfUser(int userId) {
        if (!isAlreadyActive(userId)) {
            return 0;
        }

        LocalDateTime time = activeUsersId.get(userId);
        return TIMEOUT_IN_MINUTES - (int) ChronoUnit.MINUTES.between(time, LocalDateTime.now());
    }

    public static void cleanInactiveUsers() {
        for (Map.Entry<Integer, LocalDateTime> session : activeUsersId.entrySet()) {
            if (TIMEOUT_IN_MINUTES <= ChronoUnit.MINUTES.between(session.getValue(), LocalDateTime.now())){
                removeActiveUser(session.getKey());
            }
        }
    }

    public static void print() {
        System.out.println("active users: " + activeUsersId);
    }
}
