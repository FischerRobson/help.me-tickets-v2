package com.helpme.tickets.controllers;

import com.helpme.tickets.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping
    public ResponseEntity upload(@RequestParam MultipartFile file) {
        this.uploadService.uploadFile(file);
        return ResponseEntity.ok("nice boobs");
    }

}
