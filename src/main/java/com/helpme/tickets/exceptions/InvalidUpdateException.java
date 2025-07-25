package com.helpme.tickets.exceptions;

public class InvalidUpdateException extends RuntimeException {
    public InvalidUpdateException() {
        super("Invalid Update");
    }
}
