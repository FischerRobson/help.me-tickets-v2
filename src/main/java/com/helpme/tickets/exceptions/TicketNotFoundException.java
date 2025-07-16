package com.helpme.tickets.exceptions;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException() {
        super("Ticket Not Found");
    }

}
