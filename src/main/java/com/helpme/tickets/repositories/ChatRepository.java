package com.helpme.tickets.repositories;

import com.helpme.tickets.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
