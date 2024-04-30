package src.exceptions;

public class StateNotLoadedException extends Exception {

    public StateNotLoadedException() {
        super("The state was not loaded.");
    }

    public StateNotLoadedException(String message) {
        super(message);
    }

    public StateNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateNotLoadedException(Throwable cause) {
        super(cause);
    }
}
