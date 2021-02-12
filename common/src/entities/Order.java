package entities;

import wrappers.OrderWrapper;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order extends Entity implements Cloneable, Serializable {
    private static int idCounter = 0;

    private List<Integer> rowers;
    private String activityTitle;
    private LocalDate activityDate;
    private LocalTime activityStartTime;
    private LocalTime activityEndTime;
    private LocalDateTime registerDate;
    private List<Boat.Type> boatTypes;
    private Integer registerRower;
    private boolean approvedRequest = false;
    private Integer boat;

    public Order(Integer registerRower, List<Integer> rowers, List<Boat.Type> boatTypes, LocalDate activityDate,
                 LocalTime activityStartTime, LocalTime activityEndTime, LocalDateTime registerDate, String activityTitle) {
        this.rowers = new ArrayList<>();
        this.boatTypes = new ArrayList<>();
        this.setId(++idCounter);
        this.setRowers(rowers);
        this.setActivityDate(activityDate);
        this.setBoatTypes(boatTypes);
        this.setRegisterDate(registerDate);
        this.setRegisterRower(registerRower);
        this.setActivityStartTime(activityStartTime);
        this.setActivityEndTime(activityEndTime);
        this.setActivityTitle(activityTitle);
    }

    public Order(OrderWrapper order) {
        this.setId(++idCounter);
        this.setRowers(order.getRowers());
        this.setActivityDate(order.getActivityDate());
        this.setBoatTypes(order.getBoatTypes());
        this.setActivityStartTime(order.getActivityStartTime());
        this.setActivityEndTime(order.getActivityEndTime());
        this.setActivityTitle(order.getActivityTitle());
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    @Override
    public String getEntityType() {
        return "orders";
    }

    @Override
    public void updateField(String field, Object value) {
        switch (field) {
            case "rowers":
                this.setRowers((List<Integer>) value);
                break;
            case "activityDate":
                this.setActivityDate((LocalDate) value);
                break;
            case "activityTitle":
                this.setActivityTitle((String) value);
                break;
            case "activityStartTime":
                this.setActivityStartTime((LocalTime) value);
                break;
            case "activityEndTime":
                this.setActivityEndTime((LocalTime) value);
                break;
            case "registerDate":
                this.setRegisterDate((LocalDateTime) value);
                break;
            case "boatTypes":
                this.setBoatTypes((List<Boat.Type>) value);
                break;
            case "registerRower":
                this.setRegisterRower((Integer) value);
                break;
            case "approvedRequest":
                this.setApprovedRequest((Boolean) value);
                break;
            case "boat":
                this.setBoat((Integer) value);
                break;
            default:
        }
    }

    public List<Integer> getRowers() {
        return rowers;
    }

    public void setRowers(List<Integer> rowers) {
        this.rowers = rowers;
    }

    public List<Boat.Type> getBoatTypes() {
        return boatTypes;
    }

    public void setBoatTypes(List<Boat.Type> boatTypes) {
        this.boatTypes = boatTypes;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getRegisterRower() {
        return registerRower;
    }

    public void setRegisterRower(Integer registerRower) {
        this.registerRower = registerRower;
    }

    public boolean isApprovedRequest() {
        return approvedRequest;
    }

    public void setApprovedRequest(boolean approvedRequest) {
        this.approvedRequest = approvedRequest;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public LocalTime getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(LocalTime activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public LocalTime getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(LocalTime activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public Integer getBoat() {
        return boat;
    }

    public void setBoat(Integer boat) {
        this.boat = boat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return approvedRequest == order.approvedRequest && Objects.equals(rowers, order.rowers) && Objects.equals(activityTitle, order.activityTitle) && Objects.equals(activityDate, order.activityDate) && Objects.equals(activityStartTime, order.activityStartTime) && Objects.equals(activityEndTime, order.activityEndTime) && Objects.equals(registerDate, order.registerDate) && Objects.equals(boatTypes, order.boatTypes) && Objects.equals(registerRower, order.registerRower) && Objects.equals(boat, order.boat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowers, activityTitle, activityDate, activityStartTime, activityEndTime, registerDate, boatTypes, registerRower, approvedRequest, boat);
    }

    @Override
    public Order clone() throws CloneNotSupportedException {
        Order newOrder;
        newOrder = (Order) super.clone();
        newOrder.id = ++idCounter;

        List<Integer> rowers = new ArrayList<>(this.rowers);
        newOrder.setRowers(rowers);

        return newOrder;
    }
}
