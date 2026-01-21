package com.kevin.pipeline.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "KY_DB", uniqueConstraints = {@UniqueConstraint(columnNames = "request_key")})
public class WebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Instant createdAt;

    private String clientIp;

    @Column(unique = true, nullable = false)
    private String requestKey;

    private String payload;

    public WebhookEvent() {
        this.createdAt = Instant.now();
    }

    public WebhookEvent(String clientIp) {
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
    }

    public WebhookEvent(String clientIp, String payload) {
        //this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
        this.payload = payload;
    }

    public WebhookEvent(String requestKey, String clientIp, String payload) {
        //this.id = requestKey;
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
        this.payload = payload;
        this.requestKey = requestKey;
    }

    public String getId() {return id;}
    public void setId(String newID){this.id = newID;}

    public String getRequestKey(){return this.requestKey;}
    public void setRequestKey(String key) { this.requestKey = key; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(){} //No reason to change IP manually

    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant newTime){}

    public String getPayload() { return this.payload; }
    public void setPayload(String newPayload) {this.payload = newPayload; }
}
