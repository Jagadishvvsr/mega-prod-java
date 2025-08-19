package com.example.megaservice.incident;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Page<Incident> findByStatus(Incident.Status status, Pageable pageable);
    // findAll() and findAll(Pageable) already come from JpaRepository
}
