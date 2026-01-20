package com.kevin.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.kevin.pipeline.entity.DeadLetterEvent;
import com.kevin.pipeline.entity.WebhookEvent;
import com.kevin.pipeline.metrics.IngestMetrics;
import com.kevin.pipeline.repository.DeadLetterRepository;
import com.kevin.pipeline.repository.IngestRepository;
import com.kevin.pipeline.service.DeadLetterService;
import com.kevin.pipeline.service.WebhookIngestService;

public class WebhookIngestServiceTest {

    @Mock
    private IngestRepository testRepo;

    @Mock
    private DeadLetterRepository deadLetterRepo;

    private WebhookIngestService testService;

    @InjectMocks
    private IngestMetrics testMetrics;

    @Mock
    private DeadLetterService deadLetterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMetrics = new IngestMetrics();
        //testService = new WebhookIngestService(testRepo, testMetrics, deadLetterRepo, deadLetterService);
        testService = new WebhookIngestService(testRepo, testMetrics, deadLetterService);
    }

    @Test
    void ingest_newRequest_createsRecord_andIncrementsCreatedMetric() {
        String key = "ky_test";
        String ip = "127.0.0.1";
        String payload = "{\"message\": \"Hello\"}";

        when(testRepo.findByRequestKey(key)).thenReturn(Optional.empty());
        WebhookEvent saved = new WebhookEvent(key, ip, payload);
        when(testRepo.save(any())).thenReturn(saved);
        WebhookEvent result = testService.ingest(key, ip, payload);

        assertThat(result).isNotNull();

        verify(testRepo).save(any());
        assertThat(testMetrics.createdCount()).isEqualTo(1);
        assertThat(testMetrics.replayedCount()).isEqualTo(0);
    }

    @Test
    void ingest_existingRequest_returnsExisting_andIncrementsReplayMetric() {
        String key = "ky_test";
        String ip = "127.0.0.1";
        String payload = "{\"message\": \"Hello\"}";

        WebhookEvent existing = new WebhookEvent(key, ip, payload);
        when(testRepo.findByRequestKey(key)).thenReturn(Optional.of(existing));
        WebhookEvent result = testService.ingest(key, ip, payload);

        assertThat(result).isSameAs(existing);
        verify(testRepo, never()).save(any());
        assertThat(testMetrics.replayedCount()).isEqualTo(1);
        assertThat(testMetrics.createdCount()).isEqualTo(0);
    }

    @Test
    void ingest_failure_createsDeadLetterEvent() {
        String key = "dlq_test_key";
        String ip = "127.0.0.1";

        //empty payload will throw error
        Throwable thrown = catchThrowable(() ->
                testService.ingest(
                        key,
                        ip,
                        ""
                )
        );

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
        
        // Verify that deadLetterService.record() was called with correct event
        ArgumentCaptor<DeadLetterEvent> eventCaptor = ArgumentCaptor.forClass(DeadLetterEvent.class);
        verify(deadLetterService).record(eventCaptor.capture());
        
        DeadLetterEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getRequestKey()).isEqualTo(key);
        assertThat(capturedEvent.getClientIp()).isEqualTo(ip);
        assertThat(capturedEvent.getFailureReason()).contains("IllegalArgumentException");
    }
}
