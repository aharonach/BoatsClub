package entities;

import wrappers.ActivityWrapper;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class Activity extends Entity implements Serializable {
    private static int idCounter = 0;

    private String title = "";
    private LocalTime startTime;
    private LocalTime endTime;
    private Boat.Type boatType; //init

    public Activity(String title, LocalTime startTime, LocalTime endTime, Boat.Type boatType) {
        this.setId(++idCounter);
        this.setTitle(title);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setBoatType(boatType);
    }

    public Activity(ActivityWrapper activityWrapper) {
        this.setId(++idCounter);
        this.setTitle(activityWrapper.getTitle());
        this.setStartTime(activityWrapper.getStartTime());
        this.setEndTime(activityWrapper.getEndTime());
        this.setBoatType(activityWrapper.getBoatType());
    }

    @Override
    public String getEntityType() {
        return "activities";
    }

    @Override
    public void updateField(String field, Object value) {
        switch (field) {
            case "boatType":
                this.setBoatType((Boat.Type) value);
                break;
            case "endTime":
                this.setEndTime((LocalTime) value);
                break;
            case "startTime":
                this.setStartTime((LocalTime) value);
                break;
            case "title":
                this.setTitle((String) value);
                break;
            default:
        }
    }

    public Boat.Type getBoatType() {
        return boatType;
    }

    public void setBoatType(Boat.Type boatType) {
        this.boatType = boatType;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(title, activity.title) &&
                Objects.equals(startTime, activity.startTime) &&
                Objects.equals(endTime, activity.endTime) &&
                Objects.equals(boatType, activity.boatType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startTime, endTime, boatType);
    }
}