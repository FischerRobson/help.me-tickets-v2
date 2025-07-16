package com.helpme.tickets.services;

import com.helpme.tickets.exceptions.CategoryNotFoundException;
import com.helpme.tickets.exceptions.TicketNotFoundException;
import com.helpme.tickets.model.Category;
import com.helpme.tickets.model.TicketListItem;
import com.helpme.tickets.model.dto.CreateTicketDTO;
import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.TicketStatus;
import com.helpme.tickets.model.mapper.TicketMapper;
import com.helpme.tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketsService {

    @Autowired
    TicketRepository ticketsRepository;

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    CurrentUserService currentUserService;

    @Autowired
    TicketMapper ticketMapper;

    public Ticket create(CreateTicketDTO createTicketDTO) {
        Optional<Category> categoryExists = this.categoriesService.findById(createTicketDTO.getCategoryId());

        if(categoryExists.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        Ticket newTicket = new Ticket();
        newTicket.setTitle(createTicketDTO.getTitle());
        newTicket.setDescription(createTicketDTO.getDescription());
        newTicket.setUserId(currentUserService.getUserId());
        newTicket.setCategory(categoryExists.get());
        newTicket.setTicketStatus(TicketStatus.OPEN);

        return this.ticketsRepository.save(newTicket);
    }

    public Page<TicketListItem> findAll(Pageable pageable, List<TicketStatus> statuses) {
        Page<Ticket> page;

        if (statuses == null || statuses.isEmpty()) {
            page = ticketsRepository.findAll(pageable);
        } else {
            page = ticketsRepository.findByTicketStatusIn(statuses, pageable);
        }

        return page.map(ticketMapper::toListItem);
    }

    public Ticket findById(UUID id) {
        return this.ticketsRepository.findById(id).orElseThrow(TicketNotFoundException::new);
    }



}
