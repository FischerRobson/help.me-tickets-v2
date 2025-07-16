package com.helpme.tickets.repositories;

import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findByTicketStatusIn(List<TicketStatus> status, Pageable pageable);
    Page<Ticket> findAll(Pageable pageable);
}
