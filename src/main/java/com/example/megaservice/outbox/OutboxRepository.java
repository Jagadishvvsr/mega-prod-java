package com.example.megaservice.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop20ByPublishedFalseOrderByCreatedAtAsc();
}
