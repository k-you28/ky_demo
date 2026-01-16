package com.kevin.pipeline.repository;

import com.kevin.pipeline.entity.DeadLetterEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadLetterRepository
        extends JpaRepository<DeadLetterEvent, String> {
}

