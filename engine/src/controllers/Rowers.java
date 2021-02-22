package controllers;

import data.Notifications;
import engine.BCEngine;
import entities.Boat;
import entities.Entity;
import entities.Order;
import entities.Rower;
import interfaces.RowersController;
import wrappers.Wrapper;
import utils.Validations;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.RowerWrapper;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static entities.Rower.yearsUntilExpired;

public class Rowers extends Entities implements RowersController {

    public BCEngine engine() {
        return super.engine();
    }

    @Override
    public RowerWrapper get(int id) throws RecordNotFoundException {
        return new RowerWrapper(getRecord(id));
    }

    @Override
    public Integer add(Wrapper rower) throws RecordAlreadyExistsException, InvalidInputException, RecordNotFoundException {
        RowerWrapper newRower = (RowerWrapper) rower;

        // validations
        Validations.isEmail(newRower.getEmailAddress());
        Validations.isFullName(newRower.getName());
        Validations.isValidAge(newRower.getAge());
        checkPrivateBoat(newRower);
        checkUniqueEmail(newRower);

        // Create a new rower in the memory
        Rower rowerToAdd = new Rower(newRower);
        rowerToAdd.setJoined(LocalDateTime.now());
        rowerToAdd.setExpired(LocalDateTime.now().plusYears(yearsUntilExpired));

        // update the private boat to be a private boat
        if (rowerToAdd.hasPrivateBoat() && rowerToAdd.getPrivateBoat() != null) {
            getBoats().getRecord(rowerToAdd.getPrivateBoat()).setPrivate(true);
        }

        // add to database
        engine().addRecord("rowers", rowerToAdd);
        Notifications.addUser(rowerToAdd.getId());
        return rowerToAdd.getId();
    }

    @Override
    public void update(int id, Wrapper rower) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException {
        Rower rowerToUpdate = getRecord(id);
        RowerWrapper updatedRower = (RowerWrapper) rower;

        // get updated fields (fieldName -> value)
        Map<String, Object> updatedFields = rower.updatedFields();

        checkPrivateBoat(updatedRower);

        if (updatedRower.hasPrivateBoat() != null && updatedRower.hasPrivateBoat() && updatedRower.getPrivateBoat() != null) {
            getBoats().getRecord(updatedRower.getPrivateBoat()).setPrivate(true);
        }

        // update all the fields
        for (Map.Entry<String, Object> field : updatedFields.entrySet()) {
            // validations
            switch (field.getKey()) {
                case "email":
                    Validations.isEmail((String) field.getValue());
                    checkUniqueEmail((RowerWrapper) rower);
                    break;
                case "name":
                    Validations.isFullName((String) field.getValue());
                    break;
                case "age":
                    Validations.isValidAge((Integer) field.getValue());
                    break;
            }
            rowerToUpdate.updateField(field.getKey(), field.getValue());
        }
        // update in the database
        engine().updateRecord("rowers", rowerToUpdate);
    }

    @Override
    public void delete(int id) throws RecordNotFoundException {
        Rower rower = getRecord(id);

        Order[] ordersOfRower = getOrders().findOrdersByRower(rower.getId(), false);

        for (Order order : ordersOfRower) {
            getOrders().deleteRowerFromOrder(id, order.getId());
        }

        engine().deleteRecord("rowers", rower);
        Notifications.removeUser(rower.getId());
    }

    @Override
    public Rower[] getList() {
        return engine().getList("rowers").values().toArray(new Rower[0]);
    }

    public Rower[] getUser() throws RecordNotFoundException {
        Rower[] rowers = new Rower[1];
        rowers[0] = getRecord(engine().getUser().getId());
        return rowers;
    }

    @Override
    public Rower[] filterList(Predicate<? super Entity> condition) {
        return engine().filterList("rowers", condition).values().toArray(new Rower[0]);
    }

    public void checkPrivateBoat(RowerWrapper rower) throws InvalidInputException {
        if (rower.hasPrivateBoat() != null && rower.hasPrivateBoat() && rower.getPrivateBoat() == null) {
            throw new InvalidInputException("Private boat is not set");
        }
    }

    @Override
    public void checkUniqueEmail(RowerWrapper rower) throws RecordAlreadyExistsException {
        // Check if the admin updated boat email
        Rower[] list = filterList(r -> {
            Rower rowerInList = (Rower) r;

            // skip if it's the same rower
            if (rower.getId() == rowerInList.getId()) {
                return false;
            }

            return rower.getEmailAddress().equalsIgnoreCase(rowerInList.getEmailAddress());
        });

        if (0 < list.length) {
            throw new RecordAlreadyExistsException("rower", "email '" + list[0].getEmailAddress() + "'");
        }
    }

    @Override
    public Rower getPrivateBoatOfRowers(List<Integer> rowerIds) {
        for (int rowerId : rowerIds) {
            try {
                Rower rower = getRecord(rowerId);
                if (rower.hasPrivateBoat()) {
                    Boat boat = getBoats().getRecord(rower.getPrivateBoat());
                    if (boat.getType().getMaxCapacity() >= rowerIds.size()) {
                        return rower;
                    }
                }
            } catch (RecordNotFoundException ignored) {
            }
        }
        return null;
    }

    public Rower getRecord(int id) throws RecordNotFoundException {
        return (Rower) engine().getRecord("rowers", id);
    }

    public void deletePrivateBoats(int boatId) {
        Rower[] rowers = filterList(r -> {
            Rower rowerInList = (Rower) r;
            return rowerInList.getPrivateBoat() != null && rowerInList.getPrivateBoat() == boatId;
        });

        for (Rower rower : rowers) {
            rower.setPrivateBoat(null);
        }
    }
}
