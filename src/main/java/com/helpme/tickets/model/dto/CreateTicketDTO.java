package com.helpme.tickets.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateTicketDTO {
    private String title;
    private String description;
    private UUID categoryId;
}
