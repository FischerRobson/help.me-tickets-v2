package com.helpme.tickets.exceptions;

public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super("File Upload Failed");
    }

}
