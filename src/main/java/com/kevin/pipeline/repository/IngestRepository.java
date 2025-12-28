package com.kevin.pipeline.repository;

import com.kevin.pipeline.entity.IngestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IngestRepository extends JpaRepository<IngestRecord, String> {

    public Optional<IngestRecord> findByRequestKey(String idempotencyKey);

    public Optional<IngestRecord> findTopByClientIpOrderByCreatedAtDesc(String clientIp);

}

