package controllers;

import engine.BCEngine;
import entities.Boat;
import entities.Entity;
import entities.Order;
import interfaces.BoatsController;
import wrappers.Wrapper;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.BoatWrapper;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Boats extends Entities implements BoatsController {

    public BCEngine engine() {
        return super.engine();
    }

    @Override
    public BoatWrapper get(int id) throws RecordNotFoundException {
        return new BoatWrapper(getRecord(id));
    }

    @Override
    public void update(int id, Wrapper boat) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException {
        BoatWrapper newBoat = (BoatWrapper) boat;
        Boat boatToUpdate = getRecord(id);

        // Check things before updating the record in database
        
        if (!newBoat.getName().equals(boatToUpdate.getName())) {
            checkNameUnique(newBoat);
        }

        if (newBoat.isDisabled() != boatToUpdate.isDisabled()) {
            checkDisabledBoat(newBoat);
        }

        if (!newBoat.getType().equals(boatToUpdate.getType())) {
            checkBoatAppointed(id, newBoat);
        }

        // get updated fields (fieldName -> value)
        Map<String, Object> updatedFields = boat.updatedFields();

        // update all the fields
        for (Map.Entry<String, Object> field : updatedFields.entrySet()) {
            boatToUpdate.updateField(field.getKey(), field.getValue());
        }

        // update in the database
        engine().updateRecord("boats", boatToUpdate);
    }

    @Override
    public Integer add(Wrapper boat) throws RecordAlreadyExistsException {
        BoatWrapper newBoat = (BoatWrapper) boat;

        checkNameUnique(newBoat);

        // Create a new boat in the memory
        Boat boatToAdd = new Boat(newBoat);

        // add to database
        engine().addRecord("boats", boatToAdd);
        return boatToAdd.getId();
    }

    @Override
    public void delete(int id) throws RecordNotFoundException {
        Boat boat = getRecord(id);
        engine().deleteRecord("boats", boat);

        Orders ordersController = (Orders) engine().getController("orders");
        Rowers rowersController = (Rowers) engine().getController("rowers");

        // delete orders with the deleted boat
        ordersController.deleteOrdersWithBoat(id);
        rowersController.deletePrivateBoats(id);
    }

    /**
     * Check if the boat name is unique
     *
     * @param boat boat with id
     * @throws RecordAlreadyExistsException if the same boat name found
     */
    public void checkNameUnique(BoatWrapper boat) throws RecordAlreadyExistsException {
        Map<String, Object> updatedFields = boat.updatedFields();

        // Check if the admin updated boat name
        if (null != updatedFields.get("name")) {
            // Check if boat name already exists
            if (0 < filterList(e -> ((Boat) e).getName().equalsIgnoreCase(boat.getName())).length)
                throw new RecordAlreadyExistsException("boat", "name");
        }
    }

    /**
     * Check if boat updated to disabled but exists in orders
     *
     * @param boat updated boat with id
     * @throws InvalidInputException if the condition is true
     */
    public void checkDisabledBoat(BoatWrapper boat) throws InvalidInputException {
        Map<String, Object> updatedFields = boat.updatedFields();

        Orders ordersController = (Orders) engine().getController("orders");

        if (boat.getId() != 0 && null != updatedFields.get("disabled")) {
            Order[] ordersList = ordersController.findOrdersByBoat(boat.getId(), true);
            if (0 < ordersList.length) {
                throw new InvalidInputException("The boat is used in some open orders. please remove the orders first.");
            }
        }
    }

    public void checkBoatAppointed(int id, BoatWrapper boat) throws InvalidInputException {
        Map<String, Object> updatedFields = boat.updatedFields();

        if (null != updatedFields.get("type")) {
            Order[] orders = getOrders().filterList(o -> {
                Order order = (Order) o;
                return order.isApprovedRequest() && order.getBoat() != null && order.getBoat().equals(id);
            });

            if (0 < orders.length) {
                throw new InvalidInputException("Boat is appointed to orders. Cannot change boat type.");
            }
        }
    }

    /**
     * Filter boats by type (number of oars, number of seats and if has coxswain)
     *
     * @param boatType boat type object
     * @return boats list
     */
    public Boat[] findBoatsByType(Boat.Type boatType) {
        return filterList(b -> {
            Boat boat = (Boat) b;
            return boat.getType().equals(boatType);
        });
    }

    public Boat[] findBoatByNonPrivateNonDisabled() {
        return filterList(b -> {
            Boat boat = (Boat) b;
            return !boat.isDisabled() && !boat.isPrivate();
        });
    }

    public Boat[] findBoatsByTypes(List<Boat.Type> boatTypes) {
        return filterList(b -> {
            Boat boat = (Boat) b;
            return boatTypes.contains(boat.getType()) && !boat.isDisabled() && !boat.isPrivate();
        });
    }

    public int getMaxCapacityTypeFromList(List<Boat.Type> boatTypes) {
        if (boatTypes != null) {
            return boatTypes.stream().mapToInt(Boat.Type::getMaxCapacity).max().orElseThrow(NoSuchElementException::new);
        }
        return 0;
    }

    @Override
    public Boat[] getList() {
        return engine().getList("boats").values().toArray(new Boat[0]);
    }

    @Override
    public Boat[] filterList(Predicate<? super Entity> condition) {
        return engine().filterList("boats", condition).values().toArray(new Boat[0]);
    }

    public Boat getRecord(int id) throws RecordNotFoundException {
        return (Boat) engine().getRecord("boats", id);
    }
}
