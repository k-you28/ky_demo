package com.kevin.pipeline.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "KY_DB", uniqueConstraints = {@UniqueConstraint(columnNames = "request_key")})
public class IngestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Instant createdAt;

    private String clientIp;

    @Column(unique = true, nullable = false)
    private String requestKey;

    private String userName;
    private String userMessage;

    public IngestRecord() {
        this.createdAt = Instant.now();
    }

    public IngestRecord(String clientIp) {
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
    }

    public IngestRecord(String clientIp, String name, String message) {
        //this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
        this.userName = name;
        this.userMessage = message;
    }

    public IngestRecord(String requestKey, String clientIp, String name, String message) {
        //this.id = requestKey;
        this.createdAt = Instant.now();
        this.clientIp = clientIp;
        this.userName = name;
        this.userMessage = message;
        this.requestKey = requestKey;
    }

    public String getId() {return id;}
    public void setId(String newID){this.id = newID;}

    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant newTime){} //Leaving unimplemented, no reason to change created time

    public String getClientIp() { return clientIp; }
    public void setClientIp(){} //No reason to change IP manually

    public String getRequestKey(){return this.requestKey;}
    public void setRequestKey(String key) { this.requestKey = key; }
    
    public String getUserName() { return this.userName; }
    public void setUserName(String newName) {this.userName = newName; }
    
    public String getUserMessage() { return this.userMessage; }
    public void setUserMessage(String newMessage) {this.userMessage = newMessage; }

}
