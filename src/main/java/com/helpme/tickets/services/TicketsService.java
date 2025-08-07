package com.helpme.tickets.services;

import com.helpme.tickets.exceptions.CategoryNotFoundException;
import com.helpme.tickets.exceptions.InvalidUpdateException;
import com.helpme.tickets.exceptions.TicketNotFoundException;
import com.helpme.tickets.model.Category;
import com.helpme.tickets.model.TicketListItem;
import com.helpme.tickets.model.dto.CreateTicketDTO;
import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.TicketStatus;
import com.helpme.tickets.model.dto.UpdateTicketDTO;
import com.helpme.tickets.model.helper.TicketUpdater;
import com.helpme.tickets.model.mapper.TicketMapper;
import com.helpme.tickets.producers.FileChunkProducer;
import com.helpme.tickets.repositories.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
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

    @Autowired
    FileChunkProducer fileChunkProducer;

    @Autowired
    private List<TicketUpdater> updaters;

    public Ticket create(CreateTicketDTO createTicketDTO) {
        Category category = this.categoriesService.findById(createTicketDTO.getCategoryId()).orElseThrow(() -> {
            log.warn("Category not found: {}", createTicketDTO.getCategoryId());
            return new CategoryNotFoundException();
        });

        Ticket newTicket = new Ticket();
        newTicket.setTitle(createTicketDTO.getTitle());
        newTicket.setDescription(createTicketDTO.getDescription());
        newTicket.setUserId(currentUserService.getUserId());
        newTicket.setCategory(category);
        newTicket.setTicketStatus(TicketStatus.OPEN);

        Ticket savedTicket = this.ticketsRepository.save(newTicket);
        log.info("New ticket created: {}", savedTicket.getId());
        return savedTicket;
    }

    public Ticket create(CreateTicketDTO createTicketDTO, MultipartFile[] files) throws IOException {
        Category category = this.categoriesService.findById(createTicketDTO.getCategoryId()).orElseThrow(() -> {
            log.warn("Category not found: {}", createTicketDTO.getCategoryId());
            return new CategoryNotFoundException();
        });

        Ticket newTicket = new Ticket();
        newTicket.setTitle(createTicketDTO.getTitle());
        newTicket.setDescription(createTicketDTO.getDescription());
        newTicket.setUserId(currentUserService.getUserId());
        newTicket.setCategory(category);
        newTicket.setTicketStatus(TicketStatus.OPEN);

        Ticket savedTicket = this.ticketsRepository.save(newTicket);
        log.info("New ticket created: {}", savedTicket.getId());

        for (MultipartFile file : files) {
            UUID fileId = UUID.randomUUID();
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();

            // Split file into chunks
            InputStream inputStream = file.getInputStream();
            fileChunkProducer.sendInChunks(inputStream, fileId, fileName, contentType, savedTicket.getId());
        }

        return savedTicket;
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
        return this.ticketsRepository.findById(id).orElseThrow(() -> {
            log.warn("Ticket not found: {}", id);
            return new TicketNotFoundException();
        });
    }

    public Ticket updateTicket(UpdateTicketDTO updateTicketDTO, UUID id) {
        Ticket ticket = this.ticketsRepository.findById(id).orElseThrow(() -> {
            log.warn("Ticket not found: {}", id);
            return new TicketNotFoundException();
        });
        boolean updated = false;

        for (TicketUpdater updater: updaters) {
            if (updater.supports(updateTicketDTO)) {
                updater.update(ticket, updateTicketDTO);
                updated = true;
                log.info("Ticket {} updated by {}", id, updater.getClass());
            }
        }

        if (updated) {
            return this.ticketsRepository.save(ticket);
        }

        log.warn("Invalid ticket update: {}", id);
        throw new InvalidUpdateException();
    }

}
