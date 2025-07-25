package com.helpme.tickets.config;

import com.helpme.tickets.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${MOCKED}")
    private boolean isMocked;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractJwtFromCookies(request);

        if (isMocked) {
            this.mockUser(request, response, filterChain);
            return;
        }

        if (token == null || !jwtService.isTokenValid(token)) {
            logger.warn("JWT invalid or missing");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing JWT token");
            return;
        }

        String userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);
        String sessionId = jwtService.extractSessionId(token);
        logger.info("Authenticated user: {} with role {}", userId, role);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MDC.put("userId", userId);
        MDC.put("traceId", sessionId);

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            logger.warn("Cookies are empty");
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            logger.info("Cookie: name={} value={}", cookie.getName(), cookie.getValue());
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void mockUser(HttpServletRequest request,
                          HttpServletResponse response,
                          FilterChain filterChain) throws ServletException, IOException {
        String userId = "d75b735c-40fd-4b77-ad5d-37f58d3861e9";
        String role = "ADMIN";
        String sessionId = "d863822d-7233-48cb-b9c1-da5fc4055c69";
        logger.info("Mocking user: {} with role {}", userId, role);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MDC.put("userId", userId);
        MDC.put("traceId", sessionId);

        filterChain.doFilter(request, response);
    }
}
