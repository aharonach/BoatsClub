package wrappers;

import entities.Activity;
import entities.Boat;
import entities.Entity;
import entities.Order;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderWrapper implements Wrapper, Serializable {
    private final int id;
    private final List<Integer> rowers;
    private final String activityTitle;
    private final LocalDate activityDate;
    private final LocalTime activityStartTime;
    private final LocalTime activityEndTime;
    private final List<Boat.Type> boatTypes;
    private final Boolean approvedRequest;
    private final Integer boat;

    public OrderWrapper(int id, List<Integer> rowers, String activityTitle, LocalDate activityDate,
                        LocalTime activityStartTime, LocalTime activityEndTime, List<Boat.Type> boatTypes,
                        Boolean approvedRequest, Integer boat) {
        this.id = id;
        this.rowers = rowers;
        this.activityTitle = activityTitle;
        this.activityDate = activityDate;
        this.activityStartTime = activityStartTime;
        this.activityEndTime = activityEndTime;
        this.boatTypes = boatTypes;
        this.approvedRequest = approvedRequest;
        this.boat = boat;
    }

    public OrderWrapper(Order order) {
        this.id = order.getId();
        this.rowers = order.getRowers();
        this.activityTitle = order.getActivityTitle();
        this.activityDate = order.getActivityDate();
        this.activityStartTime = order.getActivityStartTime();
        this.activityEndTime = order.getActivityEndTime();
        this.boatTypes = order.getBoatTypes();
        this.approvedRequest = order.isApprovedRequest();
        this.boat = order.getBoat();
    }

    public static OrderWrapper create(Entity entity) {
        return new OrderWrapper((Order) entity);
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public List<Integer> getRowers() {
        return rowers;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public LocalTime getActivityStartTime() {
        return activityStartTime;
    }

    public LocalTime getActivityEndTime() {
        return activityEndTime;
    }

    public List<Boat.Type> getBoatTypes() {
        return boatTypes;
    }

    public Boolean isApprovedRequest() {
        return approvedRequest;
    }

    public Integer getBoat() {
        return boat;
    }

    @Override
    public Map<String, Object> updatedFields() {
        Map<String, Object> fields = new HashMap<>();
        if (null != getRowers()) {
            fields.put("rowers", getRowers());
        }
        if (null != getActivityDate()) {
            fields.put("activityDate", getActivityDate());
        }
        if (null != getActivityTitle()) {
            fields.put("activityTitle", getActivityTitle());
        }
        if (null != getActivityStartTime()) {
            fields.put("activityStartTime", getActivityStartTime());
        }
        if (null != getActivityEndTime()) {
            fields.put("activityEndTime", getActivityEndTime());
        }
        if (null != getBoatTypes()) {
            fields.put("boatTypes", getBoatTypes());
        }
        if (null != isApprovedRequest()) {
            fields.put("approvedRequest", isApprovedRequest());
        }
        if (null != getBoat()) {
            fields.put("boat", getBoat());
        }
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderWrapper that = (OrderWrapper) o;
        return Objects.equals(rowers, that.rowers) && Objects.equals(activityTitle, that.activityTitle) && Objects.equals(activityDate, that.activityDate) && Objects.equals(activityStartTime, that.activityStartTime) && Objects.equals(activityEndTime, that.activityEndTime) && Objects.equals(boatTypes, that.boatTypes) && Objects.equals(approvedRequest, that.approvedRequest) && Objects.equals(boat, that.boat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowers, activityTitle, activityDate, activityStartTime, activityEndTime, boatTypes, approvedRequest, boat);
    }
}
