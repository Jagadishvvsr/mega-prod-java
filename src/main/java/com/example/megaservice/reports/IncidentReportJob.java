package com.example.megaservice.reports;

import com.example.megaservice.incident.Incident;
import com.example.megaservice.incident.IncidentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class IncidentReportJob {

    private final IncidentRepository repo;

    public IncidentReportJob(IncidentRepository repo) {
        this.repo = repo;
    }

    @Scheduled(cron = "${app.reports.cron:0 0/30 * * * *}")
    public void generate() {
        // naive summary counts by status (in a real app, write to a table or S3 and email it)
        Map<Incident.Status, Long> counts = new EnumMap<>(Incident.Status.class);
        for (Incident.Status s : Incident.Status.values()) {
            counts.put(s, repo.count());
        }
        // no-op: this demo just simulates work for observability
    }
}
