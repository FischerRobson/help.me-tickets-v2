package com.helpme.tickets.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helpme.common.amqp.FileChunkMessage;
import com.helpme.common.amqp.RabbitMQConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileChunkProducer {

    @Autowired
    private ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;

    public void send(FileChunkMessage chunk) {
        log.info("Sending file chunk to queue: {}", chunk);
        rabbitTemplate.convertAndSend(RabbitMQConstants.FILE_CHUNKS_QUEUE, chunk);
    }

    public void sendInChunks(InputStream inputStream, UUID fileId, String fileName, String contentType, UUID ticketId) {
        final int CHUNK_SIZE = 1024 * 256; // 256 KB
        byte[] buffer = new byte[CHUNK_SIZE];
        int bytesRead;
        int chunkIndex = 0;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // First read entire file to memory to count totalChunks (required for your constructor)
            byte[] tempBuffer = new byte[CHUNK_SIZE];
            while ((bytesRead = inputStream.read(tempBuffer)) != -1) {
                baos.write(tempBuffer, 0, bytesRead);
            }

            byte[] fileBytes = baos.toByteArray();
            int totalChunks = (int) Math.ceil((double) fileBytes.length / CHUNK_SIZE);


            // BYTE ARRAY AND JSON CONFLICTS - NEED CHANGES
            for (int i = 0; i < totalChunks; i++) {
                int start = i * CHUNK_SIZE;
                int end = Math.min(start + CHUNK_SIZE, fileBytes.length);
                byte[] chunkData = Arrays.copyOfRange(fileBytes, start, end);

                FileChunkMessage chunk = new FileChunkMessage(
                        ticketId.toString(),
                        fileId.toString(),
                        i,
                        totalChunks,
                        chunkData,
                        fileName,
                        contentType
                );

                String json = objectMapper.writeValueAsString(chunk);
                rabbitTemplate.convertAndSend("uploads.file.chunks", json);

                rabbitTemplate.convertAndSend(RabbitMQConstants.FILE_CHUNKS_QUEUE, json);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading file stream", e);
        }
    }

}