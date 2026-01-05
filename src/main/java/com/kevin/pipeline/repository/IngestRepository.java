package com.kevin.pipeline.repository;

import com.kevin.pipeline.entity.IngestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface IngestRepository extends JpaRepository<IngestRecord, String> {

    public Optional<IngestRecord> findByRequestKey(String idempotencyKey);

    public Optional<IngestRecord> findTopByClientIpOrderByCreatedAtDesc(String clientIp);

}

