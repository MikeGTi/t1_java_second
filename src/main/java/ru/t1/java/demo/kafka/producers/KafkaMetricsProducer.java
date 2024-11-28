package ru.t1.java.demo.kafka.producers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaMetricsProducer<T extends MetricStatisticDto> {

    private final KafkaTemplate template;

    @Value("${t1.kafka.topic.methods-metric}")
    private String topic;

    @Value("${t1.kafka.topic.header.methods-metric-header}")
    private String header;

    public void send(T metricStatisticDto) {
        try {
            List<Header> headers = List.of(new RecordHeader(header, header.getBytes(StandardCharsets.UTF_8)));
            ProducerRecord<String, MetricStatisticDto> record = new ProducerRecord<>(topic,
                                                                             null,
                                                                            null,
                                                                                     UUID.randomUUID().toString(),
                                                                                     metricStatisticDto,
                                                                                     headers);
            template.send(topic, record).get();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        } finally {
            template.flush();
        }

    }

}
