package interfaces;

import entities.Boat;
import entities.Entity;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.Wrapper;
import wrappers.BoatWrapper;

import java.util.List;
import java.util.function.Predicate;

public interface BoatsController extends Controller {

    BoatWrapper get(int id) throws RecordNotFoundException;

    void update(int id, Wrapper boat) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException;

    Integer add(Wrapper boat) throws RecordAlreadyExistsException;

    void delete(int id) throws RecordNotFoundException;

    /**
     * Check if the boat name is unique
     *
     * @param boat boat with id
     * @throws RecordAlreadyExistsException if the same boat name found
     */
    void checkNameUnique(BoatWrapper boat) throws RecordAlreadyExistsException;

    /**
     * Check if boat updated to disabled but exists in orders
     *
     * @param boat updated boat with id
     * @throws InvalidInputException if the condition is true
     */
    void checkDisabledBoat(BoatWrapper boat) throws InvalidInputException;

    void checkBoatAppointed(int id, BoatWrapper boat) throws InvalidInputException;

    /**
     * Filter boats by type (number of oars, number of seats and if has coxswain)
     *
     * @param boatType boat type object
     * @return boats list
     */
    Boat[] findBoatsByType(Boat.Type boatType);

    Boat[] findBoatByNonPrivateNonDisabled();

    Boat[] findBoatsByTypes(List<Boat.Type> boatTypes);

    int getMaxCapacityTypeFromList(List<Boat.Type> boatTypes);

    Boat[] getList();

    Boat[] filterList(Predicate<? super Entity> condition);

    Boat getRecord(int id) throws RecordNotFoundException;
}
