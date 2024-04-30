package src.exceptions;

public class StateNotSavedException extends Exception {

    public StateNotSavedException() {
        super("The state was not saved.");
    }

    public StateNotSavedException(String message) {
        super(message);
    }

    public StateNotSavedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateNotSavedException(Throwable cause) {
        super(cause);
    }
}
