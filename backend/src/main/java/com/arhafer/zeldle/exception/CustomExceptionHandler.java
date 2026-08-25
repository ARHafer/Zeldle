package com.arhafer.zeldle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoSuchItemException.class)
    public ProblemDetail handleNoSuchItemException(NoSuchItemException e) {
        ProblemDetail response = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        response.setTitle("No Such Item Exists");

        return response;
    }

    @ExceptionHandler(DuplicateGuessException.class)
    public ProblemDetail handleItemPreviouslyGuessedException(DuplicateGuessException e) {
        ProblemDetail response = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage()); // A 409 seems the most appropriate.
        response.setTitle("Item Was Previously Guessed");

        return response;
    }

    @ExceptionHandler(GameOverException.class)
    public ProblemDetail handleGameOverException(GameOverException e) {
        ProblemDetail response = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        response.setTitle("Game Over");

        return response;
    }

    @ExceptionHandler
    public ProblemDetail handleGameInitializationException(GameInitializationException e) {
        ProblemDetail response = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        response.setTitle("Game Initialization Error");

        return response;
    }
}
