package com.helpme.tickets.services;

import com.helpme.tickets.model.Chat;
import com.helpme.tickets.model.Ticket;
import com.helpme.tickets.model.dto.CreateChatDTO;
import com.helpme.tickets.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatsService {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    TicketsService ticketsService;

    @Autowired
    CurrentUserService currentUserService;

    public Chat create(CreateChatDTO createChatDTO) {
        Ticket ticket = this.ticketsService.findById(createChatDTO.getTicketId());

        Chat chat = new Chat();
        chat.setTicket(ticket);
        chat.setContent(createChatDTO.getContent());
        chat.setAuthorId(currentUserService.getUserId());
        chat.setFilesURL(createChatDTO.getFileURLs());

        return this.chatRepository.save(chat);
    }

}
