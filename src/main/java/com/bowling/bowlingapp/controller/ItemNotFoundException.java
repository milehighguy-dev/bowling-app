package com.bowling.bowlingapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * Throw this when something cannot be retrieved from the DB
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException{

    ItemNotFoundException(String message) {
        super(message);
    }

}
