package by.epam.task2.exception;

public class ExtendedException extends Exception {
    public ExtendedException() {
    }

    public ExtendedException(String message) {
        super(message);
    }

    public ExtendedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtendedException(Throwable cause) {
        super(cause);
    }

    public ExtendedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
