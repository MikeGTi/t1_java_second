package ru.t1.java.demo.kafka.producers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaLogDataSourceErrorProducer<T extends DataSourceErrorLog> {

    private final KafkaTemplate<String, T> template;

    @Value("${t1.kafka.topic.data-source-errors}")
    private String topic;

    @Value("${t1.kafka.topic.header.data-source-errors-header}")
    private String header;

    public void send(T err) throws Exception {
        try {
            template.setDefaultTopic(topic);
            List<Header> headers = List.of(new RecordHeader(header, header.getBytes(StandardCharsets.UTF_8)));
            ProducerRecord<String, T> record = new ProducerRecord<>(topic, null, UUID.randomUUID().toString(), err, headers);
            template.send(record).get();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            template.flush();
        }
    }
}
