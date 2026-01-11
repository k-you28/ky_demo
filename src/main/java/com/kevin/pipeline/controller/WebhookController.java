package com.kevin.pipeline.controller;

import com.kevin.pipeline.model.WebhookRequest;
import com.kevin.pipeline.service.WebhookIngestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                request.getPayloadType(),
                request.getPayload(),
                clientIp
        );
        return ResponseEntity.accepted().build();
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0] : request.getRemoteAddr();
    }
}
