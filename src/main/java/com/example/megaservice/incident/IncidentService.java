package com.example.megaservice.incident;

import com.example.megaservice.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;  // <-- add

@Service
public class IncidentService {

    private final IncidentRepository incidents;
    private final IncidentCommentRepository comments;
    private final AttachmentRepository attachments;
    private final StorageService storage;

    public IncidentService(
            IncidentRepository incidents,
            IncidentCommentRepository comments,
            AttachmentRepository attachments,
            StorageService storage
    ) {
        this.incidents = incidents;
        this.comments = comments;
        this.attachments = attachments;
        this.storage = storage;
    }

    // --- create & basic operations used by API/tests ---

    public Incident create(String title, String description, Incident.Severity severity) {
        return incidents.save(new Incident(title, description, severity));
    }

    /** Used by controller's GET /api/v1/incidents (no paging). */
    public List<Incident> findAll() {
        return incidents.findAll();
    }

    /** Optional paged listing used by unit tests. */
    public Page<Incident> list(Incident.Status status, Pageable pageable) {
        if (status == null) {
            return incidents.findAll(pageable);
        }
        return incidents.findByStatus(status, pageable);
    }

    public Incident findById(Long id) {
        return incidents.findById(id).orElseThrow();
    }

    public Incident acknowledge(Long id) {
        Incident inc = findById(id);
        inc.setStatus(Incident.Status.ACKNOWLEDGED);
        return incidents.save(inc);
    }

    public Incident resolve(Long id) {
        Incident inc = findById(id);
        inc.setStatus(Incident.Status.RESOLVED);
        return incidents.save(inc);
    }

    /** Adds a file attachment and stores the bytes using the configured StorageService. */
    public void addAttachment(Long incidentId, MultipartFile file) throws Exception {
        Incident inc = incidents.findById(incidentId).orElseThrow();

        String key = storage.store(
                "incidents/" + incidentId,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getInputStream()
        );

        attachments.save(new Attachment(
                inc,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                key
        ));
    }
}
