package com.example.megaservice.outbox;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {

    private final OutboxRepository repo;
    private final KafkaTemplate<String, String> kafka;
    private final String topic;

    public OutboxPublisher(OutboxRepository repo, KafkaTemplate<String, String> kafka,
                           @Value("${app.kafka.topic:events}") String topic) {
        this.repo = repo;
        this.kafka = kafka;
        this.topic = topic;
    }

    @Scheduled(fixedDelayString = "${app.outbox.intervalMs:5000}")
    @Transactional
    public void publish() {
        for (OutboxEvent e : repo.findTop20ByPublishedFalseOrderByCreatedAtAsc()) {
            kafka.send(new ProducerRecord<>(topic, e.getType(), e.getPayloadJson()));
            e.setPublished(true);
            repo.save(e);
        }
    }
}
