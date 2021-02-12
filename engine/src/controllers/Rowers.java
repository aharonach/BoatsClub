package controllers;

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

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Rowers extends Entities implements RowersController {

    @Override
    public RowerWrapper get(int id) throws RecordNotFoundException {
        return new RowerWrapper(getRecord(id));
    }

    @Override
    public Integer add(Wrapper rower) throws RecordAlreadyExistsException, InvalidInputException {
        RowerWrapper newRower = (RowerWrapper) rower;

        // validations
        Validations.isEmail(newRower.getEmailAddress());
        Validations.isFullName(newRower.getName());
        Validations.isValidAge(newRower.getAge());
        checkUniqueEmail(newRower);

        // Create a new rower in the memory
        Rower rowerToAdd = new Rower(newRower);

        // add to database
        engine().addRecord("rowers", rowerToAdd);
        return rowerToAdd.getId();
    }

    @Override
    public void update(int id, Wrapper rower) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException {
        Rower rowerToUpdate = getRecord(id);

        // get updated fields (fieldName -> value)
        Map<String, Object> updatedFields = rower.updatedFields();

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
    }

    @Override
    public Rower[] getList() {
        return engine().getList("rowers").values().toArray(new Rower[0]);
    }

    @Override
    public Rower[] filterList(Predicate<? super Entity> condition) {
        return engine().filterList("rowers", condition).values().toArray(new Rower[0]);
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
}
