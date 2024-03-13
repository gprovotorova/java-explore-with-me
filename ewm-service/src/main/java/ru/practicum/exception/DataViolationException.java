package ru.practicum.exception;

public class DataViolationException extends RuntimeException {
    public DataViolationException(String message) {
        super(message);
    }

    public DataViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataViolationException(Throwable cause) {
        super(cause);
    }
}
