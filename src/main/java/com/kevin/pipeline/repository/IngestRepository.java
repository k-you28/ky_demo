package com.kevin.pipeline.repository;

import com.kevin.pipeline.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface IngestRepository extends JpaRepository<WebhookEvent, String> {

    public Optional<WebhookEvent> findByRequestKey(String idempotencyKey);

    public Optional<WebhookEvent> findTopByClientIpOrderByCreatedAtDesc(String clientIp);

}

