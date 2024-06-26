package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exception) {
        log.error("Data not found ", exception);
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        String errorMessage = "Data not found error: " + exception.getMessage() + stacktrace;
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInternalServiceException(final Exception exception) {
        log.error("Server error ", exception);
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        String errorMessage = "InternalService error: " + exception.getMessage() + stacktrace;
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInternalServiceException(final DataIntegrityViolationException exception) {
        log.error("Server error ", exception);
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        String errorMessage = "InternalService error: " + exception.getMessage() + stacktrace;
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccessException(final AvailableException exception) {
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        String errorMessage = "Available error: " + exception.getMessage() + stacktrace;
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException exception) {
        String message = exception.getMessage();
        return new ErrorResponse(message);
    }
}
