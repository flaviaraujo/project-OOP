package src.exceptions;

public class ActivityIsRegisteredException extends Exception {

    public ActivityIsRegisteredException() {
        super("Activity is registered and cannot be deleted.");
    }

    public ActivityIsRegisteredException(String message) {
        super(message);
    }

    public ActivityIsRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityIsRegisteredException(Throwable cause) {
        super(cause);
    }
}
