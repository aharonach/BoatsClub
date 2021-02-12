package interfaces;

import entities.Activity;
import entities.Entity;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.ActivityWrapper;
import wrappers.Wrapper;

import java.util.function.Predicate;

public interface ActivitiesController extends Controller {
    @Override
    ActivityWrapper get(int id) throws RecordNotFoundException;

    @Override
    Integer add(Wrapper newEntity) throws RecordAlreadyExistsException, InvalidInputException;

    @Override
    void update(int id, Wrapper newEntity) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException;

    @Override
    void delete(int id) throws RecordNotFoundException;

    @Override
    Activity[] getList();

    @Override
    Activity[] filterList(Predicate<? super Entity> condition);

    void checkActivityUnique(ActivityWrapper newActivity) throws RecordAlreadyExistsException;

    @Override
    Activity getRecord(int id) throws RecordNotFoundException;
}
