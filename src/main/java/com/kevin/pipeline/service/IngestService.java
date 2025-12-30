package com.kevin.pipeline.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kevin.pipeline.model.IngestRequest;
import com.kevin.pipeline.repository.IngestRepository;
import com.kevin.pipeline.entity.IngestRecord;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
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
	
	public List<IngestRecord> getDatabaseContents() {
		return this.ingestRepository.findAll();
	}

	@Transactional
	public IngestRecord ingest(String key, String ip, String userName, String userMessage) {
		/*Optional<IngestRecord> existing =
				ingestRepository.findByRequestKey(key);

		if (existing.isPresent()) {
			System.out.print("DUP REQUEST MADE WHEN WRITING TO DB");
			return existing.get();
		}*/

		Optional<IngestRecord> lastRecordOpt = ingestRepository.findTopByClientIpOrderByCreatedAtDesc(ip);

		Instant now = Instant.now();
		if (lastRecordOpt.isPresent()) {
			Instant lastTime = lastRecordOpt.get().getCreatedAt();
			if (lastTime.plusSeconds(2).isAfter(now)) {
				throw new RuntimeException("Rate limit exceeded. Please wait before retrying.");
			}
		}


		IngestRecord record = new IngestRecord(ip, userName, userMessage);
		record.setId(UUID.randomUUID().toString());
		record.setRequestKey(key);
		return ingestRepository.save(record);
	}
}
