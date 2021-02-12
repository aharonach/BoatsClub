package controllers;

import entities.Activity;
import entities.Entity;
import interfaces.ActivitiesController;
import wrappers.Wrapper;
import utils.Validations;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.ActivityWrapper;

import java.util.Map;
import java.util.function.Predicate;

public class Activities extends Entities implements ActivitiesController {

    @Override
    public ActivityWrapper get(int id) throws RecordNotFoundException {
        return new ActivityWrapper(getRecord(id));
    }

    @Override
    public Integer add(Wrapper newEntity) throws RecordAlreadyExistsException, InvalidInputException {
        ActivityWrapper newActivity = (ActivityWrapper) newEntity;

        Validations.checkTimes(newActivity.getStartTime(), newActivity.getEndTime());
        checkActivityUnique(newActivity);

        // Create a new activity in the memory
        Activity activityToAdd = new Activity(newActivity);

        engine().addRecord("activities", activityToAdd);
        return activityToAdd.getId();
    }

    @Override
    public void update(int id, Wrapper newEntity) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException {
        ActivityWrapper activity = (ActivityWrapper) newEntity;
        Activity activityToUpdate = getRecord(id);

        // get updated fields (fieldName -> value)
        Map<String, Object> updatedFields = activity.updatedFields();

        if (updatedFields.get("endTime") != null) {
            Validations.checkTimes(activityToUpdate.getStartTime(), activity.getEndTime());
        }

        if (updatedFields.get("startTime") != null) {
            Validations.checkTimes(activity.getStartTime(), activityToUpdate.getEndTime());
        }

        // update all the fields
        for (Map.Entry<String, Object> field : updatedFields.entrySet()) {
            activityToUpdate.updateField(field.getKey(), field.getValue());
        }

        // update in the database
        engine().updateRecord("activities", activityToUpdate);
    }

    @Override
    public void delete(int id) throws RecordNotFoundException {
        Activity activity = getRecord(id);
        engine().deleteRecord("activities", activity);
    }

    @Override
    public Activity[] getList() {
        return engine().getList("activities").values().toArray(new Activity[0]);
    }

    @Override
    public Activity[] filterList(Predicate<? super Entity> condition) {
        return engine().filterList("activities", condition).values().toArray(new Activity[0]);
    }

    @Override
    public Activity getRecord(int id) throws RecordNotFoundException {
        return (Activity) engine().getRecord("activities", id);
    }

    /**
     * Check if activity is unique (different title and times)
     *
     * @param newActivity to check against
     * @throws RecordAlreadyExistsException if we found something
     */
    @Override
    public void checkActivityUnique(ActivityWrapper newActivity) throws RecordAlreadyExistsException {
        Activity[] filtered = filterList(a -> {
            Activity activity = (Activity) a;
            return activity.getEndTime().equals(newActivity.getEndTime())
                    && activity.getStartTime().equals(newActivity.getStartTime())
                    && activity.getTitle().equalsIgnoreCase(newActivity.getTitle());
        });

        if (filtered.length > 0) {
            throw new RecordAlreadyExistsException("activities",
                    "title '" + filtered[0].getTitle() + "' and times '" + filtered[0].getStartTime() + " - " + filtered[0].getEndTime() +
                            "'");
        }
    }
}
