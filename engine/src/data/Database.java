package data;

import entities.Entity;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final String[] entityTypes = {"boats", "rowers", "activities", "orders"};
    protected static Map<String, Map<Integer, Entity>> database;


    public Database() {
        init();
    }

    public static String[] getEntityTypes() {
        return entityTypes;
    }

    /**
     * Allocate Maps
     */
    private void init() {
        database = new HashMap<>(entityTypes.length);
        for (String entityType : getEntityTypes()) {
            database.put(entityType, new HashMap<>());
        }
    }

    public Map<String, Map<Integer, Entity>> getDatabase() {
        return database;
    }

    public Map<Integer, Entity> get(String entityType) {
        return database.get(entityType);
    }
}
