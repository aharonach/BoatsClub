package interfaces;

import entities.Entity;
import entities.Rower;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.RowerWrapper;
import wrappers.Wrapper;

import java.util.List;
import java.util.function.Predicate;

public interface RowersController extends Controller {
    @Override
    RowerWrapper get(int id) throws RecordNotFoundException;

    @Override
    Integer add(Wrapper rower) throws RecordAlreadyExistsException, InvalidInputException, RecordNotFoundException;

    @Override
    void update(int id, Wrapper rower) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException;

    @Override
    void delete(int id) throws RecordNotFoundException;

    @Override
    Rower[] getList() throws RecordNotFoundException;

    @Override
    Rower[] filterList(Predicate<? super Entity> condition);

    void checkUniqueEmail(RowerWrapper rower) throws RecordAlreadyExistsException;

    Rower getPrivateBoatOfRowers(List<Integer> rowerIds);

    Rower getRecord(int id) throws RecordNotFoundException;
}
