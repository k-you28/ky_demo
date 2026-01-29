package com.kevin.pipeline.controller;

import com.kevin.pipeline.entity.ApiKey;
import com.kevin.pipeline.service.ApiKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
}
