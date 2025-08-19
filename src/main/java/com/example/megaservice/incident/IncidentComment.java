package com.example.megaservice.incident;

import jakarta.persistence.*;

@Entity
@Table(name = "incident_comments")
public class IncidentComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Incident incident;

    @Column(length = 2000)
    private String text;

    public IncidentComment() {}

    public IncidentComment(Incident incident, String text) {
        this.incident = incident;
        this.text = text;
    }

    // getters & setters
    public Long getId() { return id; }
    public Incident getIncident() { return incident; }
    public String getText() { return text; }

    public void setId(Long id) { this.id = id; }
    public void setIncident(Incident incident) { this.incident = incident; }
    public void setText(String text) { this.text = text; }
}
