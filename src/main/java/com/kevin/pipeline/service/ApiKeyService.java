package com.kevin.pipeline.service;

import com.kevin.pipeline.entity.ApiKey;
import com.kevin.pipeline.repository.ApiKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * Validates an API key and updates last used timestamp
     */
    @Transactional
    public boolean isValid(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            return false;
        }

        Optional<ApiKey> keyOpt = apiKeyRepository.findByKeyValueAndActiveTrue(apiKey);
        if (keyOpt.isPresent()) {
            ApiKey key = keyOpt.get();
            // Update last used timestamp
            key.setLastUsedAt(Instant.now());
            apiKeyRepository.save(key);
            return true;
        }
        return false;
    }

    /**
     * Creates a new API key
     */
    @Transactional
    public ApiKey createApiKey(String name) {
        String keyValue = generateApiKey();
        ApiKey apiKey = new ApiKey(keyValue, name);
        return apiKeyRepository.save(apiKey);
    }

    /**
     * Generates a secure API key
     */
    private String generateApiKey() {
        // Generate a UUID-based API key with prefix
        return "ky_" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Deactivates an API key
     */
    @Transactional
    public void deactivateApiKey(String keyId) {
        apiKeyRepository.findById(keyId).ifPresent(key -> {
            key.setActive(false);
            apiKeyRepository.save(key);
        });
    }
}
