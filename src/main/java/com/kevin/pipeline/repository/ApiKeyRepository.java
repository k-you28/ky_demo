package com.kevin.pipeline.repository;

import com.kevin.pipeline.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {
    Optional<ApiKey> findByKeyValueAndActiveTrue(String keyValue);
}
