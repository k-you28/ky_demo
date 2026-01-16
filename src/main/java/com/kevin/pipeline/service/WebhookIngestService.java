package com.kevin.pipeline.service;

import com.kevin.pipeline.entity.DeadLetterEvent;
import com.kevin.pipeline.metrics.IngestMetrics;
import com.kevin.pipeline.repository.DeadLetterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.kevin.pipeline.repository.IngestRepository;
import com.kevin.pipeline.entity.WebhookEvent;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class WebhookIngestService {

	private final IngestRepository ingestRepository;
	private final IngestMetrics ingestMetrics;
	private final DeadLetterRepository deadLetterRepo;
	private final DeadLetterService deadLetterService;


	public WebhookIngestService(IngestRepository ingestRepo,
								IngestMetrics ingestMetrics,
								DeadLetterRepository deadLetterRepo,
								DeadLetterService deadLetterService) {
		this.ingestRepository = ingestRepo;
		this.ingestMetrics = ingestMetrics;
        this.deadLetterRepo = deadLetterRepo;
		this.deadLetterService = deadLetterService;
    }

	@Transactional
	public WebhookEvent ingest(
			String key,
			String ip,
			String userName,
			String userMessage
	) {

		System.out.println("AHHHHH 1");
		try {
			if (userName == null || userName.isBlank()
					|| userMessage == null || userMessage.isBlank()) {
				throw new IllegalArgumentException("Name and message required");
			}

			if (key == null || key.isBlank()) {
				throw new IllegalArgumentException("Idempotency key required");
			}

			//System.out.println("Processing request with key " + key);
			Optional<WebhookEvent> existing = ingestRepository.findByRequestKey(key);
			if (existing.isPresent()) {
				this.ingestMetrics.recordReplayed();
				return existing.get();
			}

			Optional<WebhookEvent> lastRecordOpt = ingestRepository.findTopByClientIpOrderByCreatedAtDesc(ip);
			Instant now = Instant.now();

			if (lastRecordOpt.isPresent()) {
				Instant lastTime = lastRecordOpt.get().getCreatedAt();
				if (lastTime.plusSeconds(2).isAfter(now)) {
					ingestMetrics.recordRateLimited();
					throw new IllegalStateException("Rate limit exceeded");
				}
			}

			WebhookEvent record = new WebhookEvent(ip, userName, userMessage);
			record.setRequestKey(key);
			record.setCreatedAt(now);
			this.ingestMetrics.recordCreated();
			return ingestRepository.save(record);
		} catch (Exception e) {
			System.out.println("AHHHHH 2");

			ingestMetrics.recordDeadLetter();
			DeadLetterEvent dlq = new DeadLetterEvent(
					key,
					ip,
					userMessage,
					e.getClass().getSimpleName() + ": " + e.getMessage()
			);

			System.out.println("AHHHHH 3");
			deadLetterService.record(dlq);
			throw e;
		}
	}

	//Note: Need get method to return contents properly for all data points
	public List<WebhookEvent> getDatabaseContents() {
		return this.ingestRepository.findAll();
	}

	//Extract client ip
	public String getClientIP(String clientIp) {
		WebhookEvent record = new WebhookEvent(clientIp);
		ingestRepository.save(record);
		return record.getId();
	}

}
