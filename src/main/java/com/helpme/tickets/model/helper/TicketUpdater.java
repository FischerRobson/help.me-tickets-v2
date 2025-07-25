package com.helpme.tickets.model.helper;

import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.dto.UpdateTicketDTO;

public interface TicketUpdater {
    void update(Ticket ticket, UpdateTicketDTO dto);
    boolean supports(UpdateTicketDTO dto);
}
