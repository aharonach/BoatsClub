package interfaces;

import entities.Entity;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.Wrapper;

import java.util.function.Predicate;

public interface Controller {

    /**
     * Get a record for UI using a wrapper for the record's data
     *
     * @param id record id
     * @return record wrapper data
     * @throws RecordNotFoundException if record not found
     */
    Wrapper get(int id) throws RecordNotFoundException;

    /**
     * Add new record to database
     *
     * @param newEntity the updated record wrapper with it's data
     * @throws RecordAlreadyExistsException if record already exists
     */
    Integer add(Wrapper newEntity) throws RecordAlreadyExistsException, InvalidInputException, RecordNotFoundException;

    /**
     * Update a record in the database
     *
     * @param id        record id
     * @param newEntity the new record wrapper with it's data
     * @throws RecordAlreadyExistsException if record already exists
     * @throws RecordNotFoundException      if record not found
     */
    void update(int id, Wrapper newEntity) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException;

    /**
     * Delete a record from the database
     *
     * @param id record id
     * @throws RecordNotFoundException if record not found
     */
    void delete(int id) throws RecordNotFoundException, InvalidInputException;

    /**
     * Helper method to get the array list of records from entity
     *
     * @return array of records from entity
     */
    Entity[] getList() throws RecordNotFoundException;

    /**
     * Filter a list and return array of filtered records
     *
     * @param condition filter based on this condition
     * @return array of records
     */
    Entity[] filterList(Predicate<? super Entity> condition);

    /**
     * Get a record from list
     *
     * @param id record id
     * @return the found record
     * @throws RecordNotFoundException if record doesn't exists
     */
    Entity getRecord(int id) throws RecordNotFoundException;
}
