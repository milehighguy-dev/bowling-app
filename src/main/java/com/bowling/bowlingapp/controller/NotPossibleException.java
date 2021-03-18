package com.bowling.bowlingapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Used when a user submits something that isn't possible
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotPossibleException extends RuntimeException {

    public NotPossibleException(String message) {
        super(message);
    }
}
