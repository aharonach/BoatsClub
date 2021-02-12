package exceptions;

public class RecordAlreadyExistsException extends Exception {
    private final String field;
    private final String recordType;
    private final String EXCEPTION_MESSAGE = "%s with the same %s already exists.";

    public RecordAlreadyExistsException(String recordType, String field) {
        this.recordType = recordType;
        this.field = field;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, recordType, field);
    }
}
