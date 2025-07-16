package com.helpme.tickets.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JwtService {

    @Getter
    @Value("${security.token.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private Algorithm getAlgorithm() {
        byte[] decodedSecret = Base64.getDecoder().decode(secret);
        return Algorithm.HMAC256(decodedSecret);
    }


    public boolean isTokenValid(String token) {
        try {
            JWT.require(getAlgorithm())
                    .withIssuer("helpme-auth")
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            logger.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String extractUserId(String token) {
        return decodeToken(token).getSubject(); // "sub"
    }

    public String extractRole(String token) {
        return decodeToken(token).getClaim("role").asString();
    }

    public String extractSessionId(String token) {
        return decodeToken(token).getClaim("sessionId").asString();
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(getAlgorithm())
                .withIssuer("helpme-auth")
                .build()
                .verify(token);
    }
}

