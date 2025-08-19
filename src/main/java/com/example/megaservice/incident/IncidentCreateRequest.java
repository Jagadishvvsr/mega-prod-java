package com.example.megaservice.incident;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class IncidentCreateRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Incident.Severity severity;

    public IncidentCreateRequest() {}

    public IncidentCreateRequest(String title, String description, Incident.Severity severity) {
        this.title = title;
        this.description = description;
        this.severity = severity;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Incident.Severity getSeverity() { return severity; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setSeverity(Incident.Severity severity) { this.severity = severity; }
}
