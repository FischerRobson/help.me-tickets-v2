package com.helpme.tickets.model.mapper;

import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.TicketListItem;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketListItem toListItem(Ticket ticket) {
        return new TicketListItem(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getTicketStatus(),
                ticket.getUserId(),
                ticket.getSupportId(),
                ticket.getCategory()
        );
    }
}
