package com.kevin.pipeline.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kevin.pipeline.service.ApiKeyService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String AUTH_HEADER = "Authorization";
    private final ApiKeyService apiKeyService;

    public ApiKeyAuthenticationFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Only apply authentication to GET requests to /webhooks
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.startsWith("/webhooks") && "GET".equals(method)) {
            String apiKey = extractApiKey(request);

            if (apiKey == null || !apiKeyService.isValid(apiKey)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(
                    "{\"error\":\"Unauthorized\",\"message\":\"Valid API key required. Provide X-API-Key header or Authorization: ApiKey <key>\"}"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts API key from request headers
     * Supports both X-API-Key header and Authorization: ApiKey <key> format
     */
    private String extractApiKey(HttpServletRequest request) {
        // Try X-API-Key header first
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (apiKey != null && !apiKey.isBlank()) {
            return apiKey.trim();
        }

        // Try Authorization header with ApiKey format
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith("ApiKey ")) {
            return authHeader.substring(7).trim();
        }

        return null;
    }
}
