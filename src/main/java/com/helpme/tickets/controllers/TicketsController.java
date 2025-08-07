package com.helpme.tickets.controllers;

import com.helpme.tickets.model.TicketListItem;
import com.helpme.tickets.model.dto.CreateTicketDTO;
import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.TicketStatus;
import com.helpme.tickets.model.dto.UpdateTicketDTO;
import com.helpme.tickets.services.TicketsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketsController {

    @Autowired
    TicketsService ticketsService;

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody @Valid CreateTicketDTO createTicketDTO) {
        Ticket ticket = this.ticketsService.create(createTicketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ticket> createWithFiles( @RequestPart("ticket") CreateTicketDTO dto,
                                                   @RequestPart("files") MultipartFile[] files) throws IOException {
        Ticket ticket = this.ticketsService.create(dto, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> findById(@PathVariable UUID id) {
        Ticket ticket = this.ticketsService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }

    @GetMapping
    public ResponseEntity<Page<TicketListItem>> listTickets(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) List<TicketStatus> status
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<TicketListItem> pageable = ticketsService.findAll(pageRequest, status);
        return ResponseEntity.status(HttpStatus.OK).body(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(@PathVariable UUID id, @RequestBody UpdateTicketDTO dto) {
        Ticket ticket = ticketsService.updateTicket(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }

}
