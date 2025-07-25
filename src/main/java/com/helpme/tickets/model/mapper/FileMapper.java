package com.helpme.tickets.model.mapper;

import com.helpme.tickets.model.File;
import com.helpme.tickets.model.dto.FileDTO;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public FileDTO toDTO(File file, String signedUrl) {
        return new FileDTO(
                file.getId(),
                file.getName(),
                file.getMimeType(),
                file.getSize(),
                signedUrl
        );
    }
}
