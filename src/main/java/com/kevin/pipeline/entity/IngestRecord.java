package com.kevin.pipeline.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "KY_DB")
public class IngestRecord {

    @Id
    private String id;

    private Instant createdAt;

    private String clientIp;

    private String requestKey;

    private String userName;
    private String userMessage;

    public IngestRecord() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
    }

    public IngestRecord(String clientIp) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
    }

    public IngestRecord(String clientIp, String name, String message) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
        this.userName = name;
        this.userMessage = message;
    }

    public String getId() {
        return id;
    }
    public void setId(String newID){this.id = newID;}

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getClientIp() {
        return clientIp;
    }


    public void setRequestKey(String key) {
        this.requestKey = key;
    }
}
