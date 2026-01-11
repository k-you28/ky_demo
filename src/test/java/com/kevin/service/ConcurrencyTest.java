package com.kevin.service;

import com.kevin.pipeline.KyDemoApplication;
import com.kevin.pipeline.repository.IngestRepository;
import com.kevin.pipeline.service.WebhookIngestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = KyDemoApplication.class)
class ConcurrencyTest {

    @Autowired
    private WebhookIngestService WebhookIngestService;

    @Autowired
    private IngestRepository ingestRepository;

    @Test
    void concurrent_ingest_sameKey_createsOnlyOneRecord() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(5);
        long before = ingestRepository.count();

        List<Throwable> failures =
                Collections.synchronizedList(new ArrayList<>());

        String key = "ky_test";
        String ip = "127.0.0.1";

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    start.await();
                    WebhookIngestService.ingest(key, ip, "Kevin", "Hello");
                } catch (Throwable t) {
                    failures.add(t);
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        long after = ingestRepository.count();
        assertThat(ingestRepository.count()).isEqualTo(after);
        assertThat(failures).isEmpty();
    }
}
