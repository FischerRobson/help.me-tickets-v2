package com.helpme.tickets.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FileDTO {
    private UUID id;
    private String name;
    private String mimeType;
    private Long size;
    private String signedUrl;
}
