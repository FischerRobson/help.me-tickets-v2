package com.helpme.tickets.repositories;

import com.helpme.tickets.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findAllByTicketId(UUID ticketId);
}
