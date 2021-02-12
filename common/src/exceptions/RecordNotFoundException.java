package exceptions;

import utils.Utils;

public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(String entity, int id) {
        super("Record with ID #" + id + " from " + Utils.capitalize(entity) + " does not exists.");
    }
}
