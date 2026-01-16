package com.kevin.pipeline.metrics;

import java.time.Instant;
import java.time.Duration;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IngestMetrics {

    private final AtomicLong created = new AtomicLong();
    private final AtomicLong replayed = new AtomicLong();
    private final AtomicLong rateLimited = new AtomicLong();
    private final Instant startTime;
    private final AtomicLong deadLettered = new AtomicLong();



    public IngestMetrics(){
    	this.startTime = Instant.now();
    }

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
    
    public Long appRuntime() {
    	return Duration.between(startTime, Instant.now()).toSeconds();
    }

    public void recordDeadLetter() {
        deadLettered.incrementAndGet();
    }

    public long deadLetterCount() {
        return deadLettered.get();
    }

}
