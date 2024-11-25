package ru.t1.java.demo.kafka.producers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaJsonMessageProducer {

    private final KafkaTemplate<UUID, String> template;

    public void send(String message) {
        try {
            template.sendDefault(UUID.randomUUID(), message).get();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public void sendTo(java.lang.String topic, String message) {
        try {
            template.send(topic, UUID.randomUUID(), message).get();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }
}
