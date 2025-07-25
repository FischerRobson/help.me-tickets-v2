package com.helpme.tickets.model.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateChatDTO {

    private UUID authorId;
    private String content;
    private UUID ticketId;
    private List<String> fileURLs;

}
