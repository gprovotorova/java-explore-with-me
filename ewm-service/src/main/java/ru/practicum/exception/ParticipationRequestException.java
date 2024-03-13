package ru.practicum.exception;

public class ParticipationRequestException extends RuntimeException {
    public ParticipationRequestException(String message) {
        super(message);
    }

    public ParticipationRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParticipationRequestException(Throwable cause) {
        super(cause);
    }
}
