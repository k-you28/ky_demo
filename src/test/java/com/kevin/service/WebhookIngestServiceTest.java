package com.kevin.service;

import com.kevin.pipeline.repository.IngestRepository;
import com.kevin.pipeline.service.WebhookIngestService;
import com.kevin.pipeline.metrics.IngestMetrics;
import com.kevin.pipeline.entity.WebhookEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebhookIngestServiceTest {

    @Mock
    private IngestRepository testRepo;
    private WebhookIngestService testService;
    private IngestMetrics testMetrics;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMetrics = new IngestMetrics();
        testService = new WebhookIngestService(testRepo, testMetrics);
    }

    @Test
    void ingest_newRequest_createsRecord_andIncrementsCreatedMetric() {
        String key = "ky_test";
        String ip = "127.0.0.1";

        when(testRepo.findByRequestKey(key)).thenReturn(Optional.empty());
        WebhookEvent saved = new WebhookEvent(key, ip, "Kevin", "Hello");
        when(testRepo.save(any())).thenReturn(saved);
        WebhookEvent result = testService.ingest(key, ip, "Kevin", "Hello");

        assertThat(result).isNotNull();

        verify(testRepo).save(any());
        assertThat(testMetrics.createdCount()).isEqualTo(1);
        assertThat(testMetrics.replayedCount()).isEqualTo(0);
    }

    @Test
    void ingest_existingRequest_returnsExisting_andIncrementsReplayMetric() {
        String key = "ky_test";
        String ip = "127.0.0.1";

        WebhookEvent existing = new WebhookEvent(key, ip, "Kevin", "Hello");
        when(testRepo.findByRequestKey(key)).thenReturn(Optional.of(existing));
        WebhookEvent result = testService.ingest(key, ip, "Kevin", "Hello");

        assertThat(result).isSameAs(existing);
        verify(testRepo, never()).save(any());
        assertThat(testMetrics.replayedCount()).isEqualTo(1);
        assertThat(testMetrics.createdCount()).isEqualTo(0);
    }

}
