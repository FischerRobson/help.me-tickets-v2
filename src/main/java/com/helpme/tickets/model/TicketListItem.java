package com.helpme.tickets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TicketListItem {
    private UUID id;
    private String title;
    private Instant createdAt;
    private Instant updatedAt;
    private TicketStatus ticketStatus;
    private String userId;
    private String supportId;
    private Category category;
}
