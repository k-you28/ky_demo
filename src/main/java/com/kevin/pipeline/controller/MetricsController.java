package com.kevin.pipeline.controller;

import com.kevin.pipeline.metrics.IngestMetrics;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final IngestMetrics ingestMetrics;

    public MetricsController(IngestMetrics ingestMetrics) {
        this.ingestMetrics = ingestMetrics;
    }
    
    @GetMapping
    public Map<String, String> getMetrics() {
    	String idempotency = this.idempotencyMetrics().toString();
    	String lifetime = this.appLifetimeLength();
    	
    	return Map.of(
    			"Idempotency: ", idempotency,
    			"Application Lifetime: ", lifetime
    			);
    }

    @GetMapping("/idempotency")
    public Map<String, Long> idempotencyMetrics() {
        return Map.of(
                "created", ingestMetrics.createdCount(),
                "replayed", ingestMetrics.replayedCount(),
                "rateLimited", ingestMetrics.rateLimitedCount()
        );
    }
    
    @GetMapping("/appLifetime")
    public String appLifetimeLength() {
    	return ingestMetrics.appRuntime().toString() + " seconds";
    }

}
