package ru.practicum.exception.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.exception.ObjectExistException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictDataException;
import ru.practicum.exception.ObjectValidationException;
import ru.practicum.exception.InvalidPathVariableException;
import ru.practicum.exception.DataViolationException;
import ru.practicum.exception.ParticipationRequestException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(final ObjectNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Искомый объект не был не найден.")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Неправильно составлен запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(ConflictDataException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleConflictDeleteException(final ConflictDataException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Для запрошенной операции условия не выполнены.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(ObjectValidationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationDateException(final ObjectValidationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Неправильно составлен запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(InvalidPathVariableException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleValidationException(InvalidPathVariableException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Не удалось выполнить проверку некоторых полей.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Неправильно составлен запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(DataViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleObjectExistException(DataViolationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Нарушение целостности данных.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(ObjectExistException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleObjectExistException(ObjectExistException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Объект уже существует.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }

    @ExceptionHandler(ParticipationRequestException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleAllExceptions(ParticipationRequestException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .message(e.getMessage())
                .reason("Ограничение целостности было нарушено.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return errorResponse;
    }
}

