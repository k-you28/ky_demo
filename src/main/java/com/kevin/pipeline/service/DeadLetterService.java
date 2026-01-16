package com.kevin.pipeline.service;

import com.kevin.pipeline.entity.DeadLetterEvent;
import com.kevin.pipeline.repository.DeadLetterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;


@Service
public class DeadLetterService {

    private final DeadLetterRepository deadLetterRepo;

    public DeadLetterService(DeadLetterRepository deadLetterRepo) {
        this.deadLetterRepo = deadLetterRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(DeadLetterEvent event) {
        deadLetterRepo.save(event);
    }
}

