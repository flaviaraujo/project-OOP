package src.exceptions;

public class ActivityNotFoundException extends Exception {

    public ActivityNotFoundException() {
        super("Activity not found.");
    }

    public ActivityNotFoundException(String message) {
        super(message);
    }

    public ActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityNotFoundException(Throwable cause) {
        super(cause);
    }
}
