package controllers;

import engine.BCEngine;
import entities.Boat;
import entities.Entity;
import entities.Order;
import entities.Rower;
import interfaces.OrdersController;
import wrappers.Wrapper;
import utils.Validations;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.OrderWrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Orders extends Entities implements OrdersController {

    public BCEngine engine() {
        return super.engine();
    }

    @Override
    public OrderWrapper get(int id) throws RecordNotFoundException {
        return new OrderWrapper(getRecord(id));
    }

    @Override
    public Integer add(Wrapper order) throws RecordAlreadyExistsException, InvalidInputException,
            RecordNotFoundException {
        OrderWrapper newOrder = (OrderWrapper) order;

        validateOrder(newOrder);

        // Create a new order in the memory
        Order orderToAdd = new Order(newOrder);

        // set date & user
        orderToAdd.setRegisterDate(LocalDateTime.now());
        orderToAdd.setRegisterRower(engine().getUser().getId());

        // add to database
        engine().addRecord("orders", orderToAdd);

        Rower rowerWithPrivateBoat = getRowers().getPrivateBoatOfRowers(newOrder.getRowers());
        if (rowerWithPrivateBoat != null) {
            appointBoatToOrder(rowerWithPrivateBoat.getPrivateBoat(), orderToAdd.getId());
        }

        return orderToAdd.getId();
    }

    public Integer merge(OrderWrapper orderToKeep, OrderWrapper orderToMerge) throws RecordAlreadyExistsException, InvalidInputException, RecordNotFoundException {
        //check if orders not the same!
        if (orderToKeep.equals(orderToMerge)) {
            throw new InvalidInputException("Error. Orders are the same.");
        }

        //check if the 2 orders are on the same date and time!
        if (!checkOrdersSameTimePeriod(orderToKeep, orderToMerge)) {
            throw new InvalidInputException("Error. Orders have a different activity periods.");
        }

        //check if two orders have the same boat order (boat type)!
        if (!checkOrdersHaveSameBoatType(orderToKeep, orderToMerge)) {
            throw new InvalidInputException("Error. Orders have a different boat types.");
        }

        //check if the 2 orders have enough space (checking rowers)
        if (!orderHaveSpaceForMerge(orderToKeep, orderToMerge)) {
            throw new InvalidInputException("Error. Orders can't merge (too many rowers registered).");
        }

        List<Integer> rowers = orderToKeep.getRowers();
        rowers.addAll(orderToMerge.getRowers());

        //deleting order to merge
        delete(orderToMerge.getId());

        OrderWrapper newOrder = new OrderWrapper(orderToKeep.getId(), rowers, null, null, null,
                null, null, null, null);

        update(orderToKeep.getId(), newOrder);

        return orderToKeep.getId();
    }

    public boolean checkOrdersSameTimePeriod(OrderWrapper orderToKeep, OrderWrapper orderToMerge) {
        //check if the 2 orders are on the same date and time!
        return orderToKeep.getActivityDate().equals(orderToMerge.getActivityDate()) &&
                orderToKeep.getActivityStartTime().equals(orderToMerge.getActivityStartTime()) &&
                orderToKeep.getActivityEndTime().equals(orderToMerge.getActivityEndTime());
    }

    public boolean checkOrdersHaveSameBoatType(OrderWrapper orderToKeep, OrderWrapper orderToMerge) {
        //check if two orders have the same boat order (boat type)!
        return orderToKeep.getBoatTypes().equals(orderToMerge.getBoatTypes());
    }

    public boolean orderHaveSpaceForMerge(OrderWrapper orderToKeep, OrderWrapper orderToMerge) {
        //check if the 2 orders have enough space (checking rowers)
        int maxCapacityInOrderToKeep = getBoats().getMaxCapacityTypeFromList(orderToKeep.getBoatTypes());
        int freeSpaceInOrderToKeep = maxCapacityInOrderToKeep - orderToMerge.getRowers().size();
        int rowersInOrderToMerge = orderToMerge.getRowers().size();
        return freeSpaceInOrderToKeep >= rowersInOrderToMerge;
    }

    @Override
    public void update(int id, Wrapper order) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException {

        OrderWrapper updatedOrder = (OrderWrapper) order;

        validateOrder(updatedOrder);

        Order orderToUpdate = getRecord(id);

        // get updated fields (fieldName -> value)
        Map<String, Object> updatedFields = order.updatedFields();

        // update all the fields
        for (Map.Entry<String, Object> field : updatedFields.entrySet()) {
            orderToUpdate.updateField(field.getKey(), field.getValue());
        }

        // if rowers has been updated, check if auto approve is needed
        if (updatedFields.get("rowers") != null) {
            Rower rowerWithPrivateBoat = getRowers().getPrivateBoatOfRowers(updatedOrder.getRowers());
            if (rowerWithPrivateBoat != null) {
                appointBoatToOrder(rowerWithPrivateBoat.getPrivateBoat(), orderToUpdate.getId());
            }
        }

        // update in the database
        engine().updateRecord("orders", orderToUpdate);
    }

    @Override
    public void delete(int id) throws RecordNotFoundException {
        Order order = getRecord(id);
        engine().deleteRecord("orders", order);
    }

    @Override
    public Order[] getList() {
        if (engine().isAdmin()) {
            return engine().getList("orders").values().toArray(new Order[0]);
        } else {
            return findOrdersByRower(engine().getUser().getId());
        }
    }

    @Override
    public Order[] filterList(Predicate<? super Entity> condition) {
        return engine().filterList("orders", condition).values().toArray(new Order[0]);
    }

    @Override
    public Order getRecord(int id) throws RecordNotFoundException {
        return (Order) engine().getRecord("orders", id);
    }

    public void duplicateOrder(int orderId, OrderWrapper duplicated) throws RecordNotFoundException, CloneNotSupportedException, InvalidInputException {
        OrderWrapper orderToDuplicate = get(orderId);

        if (duplicated.equals(orderToDuplicate)) {
            throw new CloneNotSupportedException("The order needs to change for duplication.");
        }

        Order originalOrder = getRecord(orderId);
        Order duplicateOrder = originalOrder.clone();

        // get updated fields (fieldName -> value)
        Map<String, Object> updatedFields = duplicated.updatedFields();

        // update all the fields
        for (Map.Entry<String, Object> field : updatedFields.entrySet()) {
            duplicateOrder.updateField(field.getKey(), field.getValue());
        }

        engine().addRecord("orders", duplicateOrder);
    }

    /**
     * Find orders of a rower based on approved/not approved field
     *
     * @param rowerId the rower id
     * @return orders
     */
    public Order[] findOrdersByRower(int rowerId, boolean approvedRequests) {
        if (!engine().isAdmin()) {
            rowerId = engine().getUser().getId();
        }
        int finalRowerId = rowerId;
        return filterList(o -> {
            Order order = (Order) o;
            return order.getRowers().contains(finalRowerId) && approvedRequests == order.isApprovedRequest();
        });
    }

    /**
     * Find all orders of a rower
     *
     * @param rowerId the rower id
     * @return orders
     */
    public Order[] findOrdersByRower(int rowerId) {
        return filterList(o -> {
            Order order = (Order) o;
            return order.getRegisterRower() == rowerId || (order.getRowers() != null && order.getRowers().contains(rowerId));
        });
    }

    /**
     * Get all orders which created by a specific rower
     * @param rowerId the rower id
     * @return orders
     */
    public Order[] findOrdersCreatedByRower(int rowerId) {
        return filterList(o -> {
            Order order = (Order) o;
            return order.getRegisterRower() == rowerId;
        });
    }

    public List<Integer> findOrderRowers(Order order) {
        return order.getRowers();
    }

    /**
     * Find orders by activity date
     *
     * @param date activity date
     * @return orders
     */
    public Order[] findOrdersByDate(LocalDate date) {
        if (engine().isAdmin()) {
            return filterList(o -> {
                Order order = (Order) o;
                return order.getActivityDate().equals(date);
            });
        }
        return findOrdersByDateOfRower(engine().getUser().getId(), date);
    }

    /**
     * Find orders by activity date
     *
     * @param date activity date
     * @return orders
     */
    public Order[] findOrdersByDateOfRower(int rowerId, LocalDate date) {
        return filterList(o -> {
            Order order = (Order) o;
            return order.getRowers() != null && order.getRowers().contains(rowerId) && order.getActivityDate().equals(date);
        });
    }

    /**
     * Find approved orders of specific rower by activity date
     *
     * @param date activity date
     * @return orders
     */
    public Order[] findApprovedOrdersByDateOfRower(int rowerId, LocalDate date) {
        return filterList(o -> {
            Order order = (Order) o;
            return order.getRowers() != null && order.getRowers().contains(rowerId) && order.getActivityDate().equals(date) && order.isApprovedRequest();
        });
    }

    public Order[] findOrdersFromLastWeek() {
        return findOrdersFromDateToDate(LocalDate.now().minusDays(7), LocalDate.now());
    }

    public Order[] findOrdersFromDateToDate(LocalDate fromDate, LocalDate afterDate) {
        fromDate = fromDate.minusDays(1); //so the day of the fromDate will also count
        afterDate = afterDate.plusDays(1); //so the day of the afterDate will also count
        LocalDate finalFromDate = fromDate;
        LocalDate finalAfterDate = afterDate;

        if (engine().isAdmin()) {
            return filterList(o -> {
                Order order = (Order) o;
                boolean isAfter = order.getActivityDate().isAfter(finalFromDate),
                        isBefore = order.getActivityDate().isBefore(finalAfterDate);
                return isBefore && isAfter;
            });
        }

        return findOrdersFromDateToDateOfRower(engine().getUser().getId(), finalFromDate, finalAfterDate);
    }

    public Order[] findOrdersFromDateToDateOfRower(int rowerId, LocalDate finalFromDate, LocalDate finalAfterDate) {
        return filterList(o -> {
            Order order = (Order) o;
            boolean isAfter = order.getActivityDate().isAfter(finalFromDate),
                    isBefore = order.getActivityDate().isBefore(finalAfterDate);
            return isBefore && isAfter && order.getRowers() != null && order.getRowers().contains(rowerId);
        });
    }

    /**
     * Find not-yes approved orders with a specific boat
     *
     * @param boatId boat id
     * @return orders
     */
    public Order[] findOrdersByBoat(int boatId, boolean approvedRequest) {
        return filterList(o -> {
            Order order = (Order) o;
            return order.getBoat() != null && order.getBoat() == boatId && approvedRequest == order.isApprovedRequest();
        });
    }

    public void deleteOrdersWithBoat(int boatId) {
        Order[] ordersList = findOrdersByBoat(boatId, false);
        for (Order o : ordersList) {
            try {
                delete(o.getId());
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addRowerToOrder(int rowerId, int orderId) throws RecordNotFoundException, InvalidInputException {
        Order order = this.getRecord(orderId);
        int boatTypeCapacity;

        if (order.getBoat() != null) {
            boatTypeCapacity = getBoats().getRecord(order.getBoat()).getType().getMaxCapacity();
            checkBoatTypeCapacity(boatTypeCapacity, order.getRowers());
        } else {
            int maxCapacity = getBoats().getMaxCapacityTypeFromList(order.getBoatTypes());
            checkBoatTypeCapacity(maxCapacity, order.getRowers());
        }

        checkRowerSameOrder(rowerId, orderId);

        order.getRowers().add(rowerId);
        engine().updateRecord("orders", order);
    }

    public void deleteRowerFromOrder(int rowerId, int orderId) throws RecordNotFoundException {
        Order order = this.getRecord(orderId);
        order.getRowers().remove((Object) rowerId);

        if (order.getRowers().isEmpty()) {
            engine().deleteRecord("orders", order);
        } else {
            engine().updateRecord("orders", order);
        }
    }

    public void appointBoatToOrder(int boatId, int orderId) throws RecordNotFoundException, InvalidInputException {
        Order order = this.getRecord(orderId);
        Boat boat = getBoats().getRecord(boatId);

        checkRowersCapacityForBoat(boat.getType().getMaxCapacity(), order.getRowers());
        checkBoatCapacityForOrder(boat.getType().getMaxCapacity(), order.getRowers());
        checkBoatSameOrder(boatId, orderId);

        order.setBoat(boatId);
        order.setApprovedRequest(true);
        engine().updateRecord("orders", order);
    }

    /**
     * Validate all the things before inserting/updating an order
     *
     * @param order order wrapper before updating in database
     * @throws InvalidInputException when error occurred
     */
    public void validateOrder(OrderWrapper order) throws InvalidInputException {
        //no rowers - order is valid
        if (order.getRowers() == null)
            return;

        // check if number of rowers doesnt exceed the max capacity of boat type
        checkRowersCapacityForBoat(
                getBoats().getMaxCapacityTypeFromList(order.getBoatTypes()),
                order.getRowers()
        );

        // check times
        Validations.checkTimes(order.getActivityStartTime(), order.getActivityEndTime());

        // check if rowers has registered orders already
        for (Object rowerId : order.getRowers().toArray()) {
            Order[] ordersOfRower = findApprovedOrdersByDateOfRower((Integer) rowerId, order.getActivityDate());
            for (Order orderOfRower : ordersOfRower) {
                checkRowerSameOrder((Integer) rowerId, orderOfRower.getId());
            }
        }
    }

    /**
     * Check if a rower has overlapping orders based on specific order
     *
     * @param rowerId rower id
     * @param orderId order id which the rower is registered
     * @throws InvalidInputException overlapping orders have found
     */
    public void checkRowerSameOrder(int rowerId, int orderId) throws InvalidInputException {
        Order[] ordersByRower = filterList(o -> {
            Order orderInList = (Order) o;
            return orderInList.getRowers().contains(rowerId) && checkOrdersOverlappingTimes(orderId, orderInList.getId());
        });

        if (ordersByRower.length > 0) {
            throw new InvalidInputException(
                    "The rower already has overlapping order (id: " + ordersByRower[0].getId() +
                            ") with the same activity times.");
        }
    }

    /**
     * Check if a boat has overlapping orders based on specific order
     *
     * @param boatId  boat id
     * @param orderId order is which the boat is registered
     * @throws InvalidInputException overlapping orders have found
     */
    public void checkBoatSameOrder(int boatId, int orderId) throws InvalidInputException {
        Order[] ordersByBoat = findOrdersByBoat(boatId, true);

        for (Order orderByBoat : ordersByBoat) {
            if (checkOrdersOverlappingTimes(orderId, orderByBoat.getId())) {
                throw new InvalidInputException(
                        "The boat already has overlapping order (id: " + orderByBoat.getId() +
                                ") with the same activity times.");
            }
        }
    }

    public void checkBoatTypeCapacity(int maxCapacityBoat, List<Integer> rowerIds) throws InvalidInputException {
        if (maxCapacityBoat < rowerIds.size() + 1) {
            throw new InvalidInputException("Cannot add rower. Max capacity of boat in order has been reached.");
        }
    }

    public void checkRowersCapacityForBoat(int maxCapacityBoat, List<Integer> rowerIds) throws InvalidInputException {
        if (maxCapacityBoat < rowerIds.size()) {
            throw new InvalidInputException("Additional rowers should be added in order to appoint this boat type.");
        }
    }

    public void checkBoatCapacityForOrder(int maxCapacityBoat, List<Integer> rowerIds) throws InvalidInputException {
        if (maxCapacityBoat > rowerIds.size()) {
            throw new InvalidInputException("Boat cannot be appointed because it doesn't have enough rowers.");
        }
    }

    public boolean checkOrdersOverlappingTimes(int orderId, int orderToCheckId) {
        try {
            Order order = getRecord(orderId),
                    orderInList = getRecord(orderToCheckId);

            LocalDate dateEditedOrder = order.getActivityDate();
            LocalTime startTimeEditedOrder = order.getActivityStartTime(),
                    endTimeEditedOrder = order.getActivityEndTime();

            LocalTime startTime = orderInList.getActivityStartTime(),
                    endTime = orderInList.getActivityEndTime();
            LocalDate date = orderInList.getActivityDate();

            if (!date.equals(dateEditedOrder)) {
                return false;
            }

            if (startTime.isAfter(endTimeEditedOrder)) {
                return false;
            }

            return !endTime.isBefore(startTimeEditedOrder);
        } catch (RecordNotFoundException ignored) {
            return false;
        }
    }
}
