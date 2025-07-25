package com.helpme.tickets.model.helper;

import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.dto.UpdateTicketDTO;
import org.springframework.stereotype.Component;

@Component
public class SupportIdUpdater implements TicketUpdater {

    @Override
    public void update(Ticket ticket, UpdateTicketDTO dto) {
        ticket.setSupportId(dto.getSupportId());
    }

    @Override
    public boolean supports(UpdateTicketDTO dto) {
        return dto.getSupportId() != null;
    }
}
