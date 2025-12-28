package com.kevin.pipeline.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kevin.pipeline.model.IngestRequest;
import com.kevin.pipeline.repository.IngestRepository;
import com.kevin.pipeline.entity.IngestRecord;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class IngestService {

	private final IngestRepository ingestRepository;
	
	public IngestService(IngestRepository ingestRepo) {
		this.ingestRepository = ingestRepo;
	}


	public String getClientIP(String clientIp) {
		IngestRecord record = new IngestRecord(clientIp);
		ingestRepository.save(record);
		return record.getId();
	}

	@Transactional
	public IngestRecord ingest(String key, String ip) {

		Optional<IngestRecord> existing =
				ingestRepository.findByRequestKey(key);

		if (existing.isPresent()) {
			return existing.get();
		}

		IngestRecord record = new IngestRecord();
		record.setId(UUID.randomUUID().toString());
		record.setRequestKey(key);
		return ingestRepository.save(record);
	}
}
