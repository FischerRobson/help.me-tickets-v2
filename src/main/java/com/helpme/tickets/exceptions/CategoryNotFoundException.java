package com.helpme.tickets.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("Category Not Found!");
    }
}
