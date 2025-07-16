package com.helpme.tickets.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateTicketDTO {
    private String title;
    private String description;
    private String userId;
    private UUID categoryId;
}
