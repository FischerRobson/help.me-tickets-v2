package com.helpme.tickets.model.dto;

import com.helpme.tickets.model.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UpdateTicketDTO {
    private TicketStatus ticketStatus;
    private UUID supportId;
}
