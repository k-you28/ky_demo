package com.kevin.pipeline.controller;

import java.util.List;

import com.kevin.pipeline.entity.WebhookEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import com.kevin.pipeline.service.WebhookIngestService;

@RestController
@RequestMapping("/webhook")
public class IngestController {

    private final WebhookIngestService WebhookIngestService;

    public IngestController(WebhookIngestService WebhookIngestService) {

        this.WebhookIngestService = WebhookIngestService;
    }

    @GetMapping
    public List<WebhookEvent> ingest(HttpServletRequest request) {
    	return WebhookIngestService.getDatabaseContents();
    }

    @PostMapping
    public ResponseEntity<?> ingest(@RequestHeader("Idempotency-Key") String RequestKey, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        WebhookEvent record = WebhookIngestService.ingest(RequestKey, ip, ip, "Post Request Processing...");
        return ResponseEntity.ok(record.getId());
    }


    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
