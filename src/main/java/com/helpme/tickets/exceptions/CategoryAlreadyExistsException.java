package com.helpme.tickets.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException() {
        super("Category already exists");
    }

}
