package com.example.megaservice.incident;

import jakarta.persistence.*;

@Entity
@Table(name = "incidents")
public class Incident {

    public enum Status { OPEN, ACKNOWLEDGED, RESOLVED }
    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    public Incident() {}
    public Incident(String title, String description, Severity severity) {
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = Status.OPEN;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Severity getSeverity() { return severity; }
    public Status getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public void setStatus(Status status) { this.status = status; }
}
