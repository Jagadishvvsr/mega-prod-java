package com.example.megaservice.incident;

import jakarta.persistence.*;

@Entity
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Incident incident;

    private String filename;
    private String contentType;
    private long size;
    private String storageKey;

    public Attachment() {}

    public Attachment(Incident incident, String filename, String contentType, long size, String storageKey) {
        this.incident = incident;
        this.filename = filename;
        this.contentType = contentType;
        this.size = size;
        this.storageKey = storageKey;
    }

    // getters & setters
    public Long getId() { return id; }
    public Incident getIncident() { return incident; }
    public String getFilename() { return filename; }
    public String getContentType() { return contentType; }
    public long getSize() { return size; }
    public String getStorageKey() { return storageKey; }

    public void setId(Long id) { this.id = id; }
    public void setIncident(Incident incident) { this.incident = incident; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setSize(long size) { this.size = size; }
    public void setStorageKey(String storageKey) { this.storageKey = storageKey; }
}
