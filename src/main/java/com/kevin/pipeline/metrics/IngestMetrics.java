package com.kevin.pipeline.metrics;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IngestMetrics {

    private final AtomicLong created = new AtomicLong();
    private final AtomicLong replayed = new AtomicLong();
    private final AtomicLong rateLimited = new AtomicLong();

    public IngestMetrics(){}

    public void recordCreated() {
        created.incrementAndGet();
    }

    public void recordReplayed() {
        replayed.incrementAndGet();
    }

    public void recordRateLimited() {
        rateLimited.incrementAndGet();
    }

    public long createdCount() {
        return created.get();
    }

    public long replayedCount() {
        return replayed.get();
    }

    public long rateLimitedCount() {
        return rateLimited.get();
    }
}
