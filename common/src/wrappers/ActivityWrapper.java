package wrappers;

import entities.Activity;
import entities.Boat;
import entities.Entity;
import entities.Rower;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


public class ActivityWrapper implements Wrapper, Serializable {
    private final String title;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Boat.Type boatType; //init
    private int id = 0;

    public ActivityWrapper(String title, LocalTime startTime, LocalTime endTime, Boat.Type boatType) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.boatType = boatType;
    }

    public ActivityWrapper(Activity activity) {
        this.id = activity.getId();
        this.title = activity.getTitle();
        this.startTime = activity.getStartTime();
        this.endTime = activity.getEndTime();
        this.boatType = activity.getBoatType();
    }

    public static ActivityWrapper create(Entity entity) {
        return new ActivityWrapper((Activity) entity);
    }

    @Override
    public int getId() {
        return this.id;
    }

    public Boat.Type getBoatType() {
        return boatType;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Map<String, Object> updatedFields() {
        Map<String, Object> fields = new HashMap<>();
        if (null != getBoatType()) {
            fields.put("boatType", getBoatType());
        }
        if (null != getEndTime()) {
            fields.put("endTime", getEndTime());
        }
        if (null != getStartTime()) {
            fields.put("startTime", getStartTime());
        }
        if (null != getTitle()) {
            fields.put("title", getTitle());
        }
        return fields;
    }
}
