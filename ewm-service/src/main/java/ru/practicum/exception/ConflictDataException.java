package ru.practicum.exception;

public class ConflictDataException extends RuntimeException {
    public ConflictDataException(String message) {
        super(message);
    }

    public ConflictDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConflictDataException(Throwable cause) {
        super(cause);
    }
}
