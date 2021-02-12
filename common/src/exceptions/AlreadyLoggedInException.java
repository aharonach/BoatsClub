package exceptions;

public class AlreadyLoggedInException extends RuntimeException {
    public AlreadyLoggedInException(String userEmail, int timeout) {
        super("User " + userEmail + " is already logged in. try again in " + timeout + " minutes.");
    }
}
