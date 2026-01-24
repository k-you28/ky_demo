/*ackage com.kevin.pipeline.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.pipeline.entity.WebhookEvent;
import com.kevin.pipeline.service.WebhookIngestService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ingest")
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
        WebhookEvent record = WebhookIngestService.ingest(RequestKey, ip, "Post Request Processing...");
        return ResponseEntity.ok(record.getId());
    }


    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}*/
