package com.kevin.service;

import com.kevin.pipeline.repository.IngestRepository;
import com.kevin.pipeline.service.IngestService;
import com.kevin.pipeline.metrics.IngestMetrics;
import com.kevin.pipeline.entity.IngestRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IngestServiceTest {

    @Mock
    private IngestRepository testRepo;
    private IngestService testService;
    private IngestMetrics testMetrics;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMetrics = new IngestMetrics();
        testService = new IngestService(testRepo, testMetrics);
    }

    @Test
    void ingest_newRequest_createsRecord_andIncrementsCreatedMetric() {
        String key = "ky_test";
        String ip = "127.0.0.1";

        when(testRepo.findByRequestKey(key)).thenReturn(Optional.empty());
        IngestRecord saved = new IngestRecord(key, ip, "Kevin", "Hello");
        when(testRepo.save(any())).thenReturn(saved);
        IngestRecord result = testService.ingest(key, ip, "Kevin", "Hello");

        assertThat(result).isNotNull();

        verify(testRepo).save(any());
        assertThat(testMetrics.createdCount()).isEqualTo(1);
        assertThat(testMetrics.replayedCount()).isEqualTo(0);
    }

    @Test
    void ingest_existingRequest_returnsExisting_andIncrementsReplayMetric() {
        String key = "ky_test";
        String ip = "127.0.0.1";

        IngestRecord existing = new IngestRecord(key, ip, "Kevin", "Hello");
        when(testRepo.findByRequestKey(key)).thenReturn(Optional.of(existing));
        IngestRecord result = testService.ingest(key, ip, "Kevin", "Hello");

        assertThat(result).isSameAs(existing);
        verify(testRepo, never()).save(any());
        assertThat(testMetrics.replayedCount()).isEqualTo(1);
        assertThat(testMetrics.createdCount()).isEqualTo(0);
    }

}
