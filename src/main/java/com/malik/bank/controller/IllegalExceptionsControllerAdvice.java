package com.malik.bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = IllegalExceptionControllerProcessing.class)
class IllegalExceptionsControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(IllegalExceptionsControllerAdvice.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleIllegalArgument(IllegalArgumentException e) {
        LOGGER.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleIllegalState(IllegalStateException e) {
        LOGGER.error(e.getMessage());
        return e.getMessage();
    }
}