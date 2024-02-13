package com.example.springbootmongodb.infra.rest;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class JokeRestExceptionHandler  {
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getMessage().contains(JokeController.COUNT_TOO_LOW_MESSAGE) ? JokeController.COUNT_TOO_LOW_MESSAGE : JokeController.COUNT_TOO_HIGH_MESSAGE;
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handeException(Exception e) {
        log.error("Encountered an exception", e);
        return ResponseEntity.internalServerError().body("Internal server error");
    }
}
