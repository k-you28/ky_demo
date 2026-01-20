package com.kevin.pipeline.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.pipeline.model.WebhookRequest;
import com.kevin.pipeline.service.WebhookIngestService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private final WebhookIngestService webhookService;

    public WebhookController(WebhookIngestService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/{source}")
    public ResponseEntity<Void> receiveWebhook(
            @PathVariable String source,
            @RequestBody WebhookRequest request,
            HttpServletRequest servletRequest
    ) {
        String clientIp = extractClientIp(servletRequest);

        webhookService.ingest(
                request.getEventID(),
                clientIp,
                request.getPayload()
        );
        return ResponseEntity.accepted().build();
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0] : request.getRemoteAddr();
    }
}
