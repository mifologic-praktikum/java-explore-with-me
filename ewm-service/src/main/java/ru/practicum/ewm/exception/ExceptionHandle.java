package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(error, BAD_REQUEST));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConflictException(final ConflictException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error, CONFLICT));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error, NOT_FOUND));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error, CONFLICT));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleParticipationRequestException(final ParticipationRequestException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error, CONFLICT));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error, CONFLICT));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(error, BAD_REQUEST));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String error = e.getMessage();
        log.error(error);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(error, BAD_REQUEST));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleThrowable(final Throwable e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}