package com.example.megaservice.outbox;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "outbox_events", indexes = @Index(name="idx_outbox_published", columnList = "published"))
public class OutboxEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=80)
    private String type;

    @Column(nullable=false, length=2000)
    private String payloadJson;

    @Column(nullable=false)
    private boolean published = false;

    @Column(nullable=false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @PrePersist public void pre(){ this.createdAt = OffsetDateTime.now(); }

    // getters/setters
    public Long getId(){ return id; }
    public String getType(){ return type; }
    public void setType(String type){ this.type = type; }
    public String getPayloadJson(){ return payloadJson; }
    public void setPayloadJson(String payloadJson){ this.payloadJson = payloadJson; }
    public boolean isPublished(){ return published; }
    public void setPublished(boolean published){ this.published = published; }
    public OffsetDateTime getCreatedAt(){ return createdAt; }
}
