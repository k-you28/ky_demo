package com.kevin.pipeline.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kevin.pipeline.entity.DeadLetterEvent;
import com.kevin.pipeline.repository.DeadLetterRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class DeadLetterService {

    private final DeadLetterRepository deadLetterRepo;
    
    @PersistenceContext
    private EntityManager entityManager;

    public DeadLetterService(DeadLetterRepository deadLetterRepo) {
        this.deadLetterRepo = deadLetterRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(DeadLetterEvent event) {
        System.out.println("Recording dead letter event: " + event.getRequestKey() + " - " + event.getFailureReason());
        // Save the entity
        DeadLetterEvent saved = deadLetterRepo.save(event);
        // Explicitly flush to ensure it's persisted before outer transaction rolls back
        entityManager.flush();
        System.out.println("Dead letter event saved with ID: " + saved.getId());
    }
}

