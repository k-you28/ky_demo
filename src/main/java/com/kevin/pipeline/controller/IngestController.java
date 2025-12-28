package com.kevin.pipeline.controller;

import java.util.Map;
import com.kevin.pipeline.entity.IngestRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import com.kevin.pipeline.model.IngestRequest;
import com.kevin.pipeline.service.IngestService;

@RestController
@RequestMapping("/ingest")
public class IngestController {

    private final IngestService ingestService;

    public IngestController(IngestService ingestService) {

        this.ingestService = ingestService;
    }

    @GetMapping
    public Map<String, String> ingest(HttpServletRequest request) {
        String clientIp = extractClientIp(request);
        String id = ingestService.getClientIP(clientIp);
        return Map.of("requestId", id);
    }

    @PostMapping
    public ResponseEntity<?> ingest(@RequestHeader("Idempotency-Key") String RequestKey, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        IngestRecord record = ingestService.ingest(RequestKey, ip);
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


