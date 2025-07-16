package com.helpme.tickets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ServerError {
    private String message;
    private int code;
    private String method;
    private String path;
    private Instant timestamp;
}
