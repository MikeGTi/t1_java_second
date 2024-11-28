package ru.t1.java.demo.kafka.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.service.impl.ClientServiceImpl;
import ru.t1.java.demo.util.ClientMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaClientConsumer {

    private final ClientServiceImpl clientServiceImpl;

    @KafkaListener(id = "${t1.kafka.topic.client-registration}",
                   topics = "${t1.kafka.topic.client-registration}",
                   containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<ClientDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Client consumer: Обработка новых сообщений");

        try {
            List<Client> clients = messageList.stream()
                    .map(dto -> {
                        dto.setFirstName(key + "@" + dto.getFirstName());
                        return ClientMapper.toEntity(dto);
                    })
                    .toList();
            clientServiceImpl.register(clients);
        } finally {
            ack.acknowledge();
        }

        log.debug("Client consumer: записи обработаны");
    }
}
