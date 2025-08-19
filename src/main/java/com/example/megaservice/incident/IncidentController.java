package com.example.megaservice.incident;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
    value = "/api/v1/incidents",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class IncidentController {

    private final IncidentService service;

    public IncidentController(IncidentService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Incident create(@RequestBody @Valid IncidentCreateRequest req) {
        return service.create(req.getTitle(), req.getDescription(), req.getSeverity());
    }

    @GetMapping
    public List<Incident> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Incident get(@PathVariable Long id) {
        return service.findById(id);
    }

    // --- Acknowledge: allow POST (no body) and PATCH. Always return a JSON body.
    @PostMapping(path = "/{id}/acknowledge", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping(path = "/{id}/acknowledge", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Incident> acknowledge(@PathVariable("id") Long id) {
        Incident updated = service.acknowledge(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updated);
    }

    // --- Resolve: allow POST (no body) and PATCH. Always return a JSON body.
    @PostMapping(path = "/{id}/resolve", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping(path = "/{id}/resolve", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Incident> resolve(@PathVariable("id") Long id) {
        Incident updated = service.resolve(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updated);
    }
}
