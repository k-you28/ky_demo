package com.kevin.pipeline.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/{source}")
    public ResponseEntity<?> getWebhookByrequestKey(
            @RequestParam String requestKey
    ) {

        if (requestKey.isBlank()) {
            return ResponseEntity.badRequest().body("Request key is required");
        }
        return webhookService.getByrequestKey(requestKey)
                .map(event -> ResponseEntity.ok(event))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{source}")
    //public ResponseEntity<Void> receiveWebhook(
    public String receiveWebhook(
            @PathVariable String source,
            @RequestBody WebhookRequest request,
            HttpServletRequest servletRequest
    ) {
        String clientIp = extractClientIp(servletRequest);

        webhookService.ingest(
                request.getrequestKey(),
                clientIp,
                request.getPayload()
        );
        //return ResponseEntity.accepted().build();
        return request.getrequestKey() + "\n";
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0] : request.getRemoteAddr();
    }
}
