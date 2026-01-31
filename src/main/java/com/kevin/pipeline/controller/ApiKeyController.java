package com.kevin.pipeline.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.pipeline.entity.ApiKey;
import com.kevin.pipeline.service.ApiKeyService;

@RestController
@RequestMapping("/admin/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    /**
     * Create a new API key
     * POST /admin/api-keys
     * Body: {"name": "My API Key"}
     */
    @PostMapping
    public ResponseEntity<ApiKey> createApiKey(@RequestBody Map<String, String> request) {
        String name = request.getOrDefault("name", "Unnamed API Key");
        ApiKey apiKey = apiKeyService.createApiKey(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKey);
    }

    /**
     * Deactivate an API key
     * DELETE /admin/api-keys/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateApiKey(@PathVariable String id) {
        apiKeyService.deactivateApiKey(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all API keys
     * GET /admin/api-keys
     */
    @GetMapping
    public ResponseEntity<List<ApiKey>> getApiKeys() {
        return apiKeyService.getApiKeys();
    }
}
