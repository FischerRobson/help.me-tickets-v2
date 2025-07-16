package com.helpme.tickets.services;

import com.helpme.tickets.exceptions.CategoryNotFoundException;
import com.helpme.tickets.model.Category;
import com.helpme.tickets.model.CreateTicketDTO;
import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.TicketStatus;
import com.helpme.tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketsService {

    @Autowired
    TicketRepository ticketsRepository;

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    CurrentUserService currentUserService;

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

}
