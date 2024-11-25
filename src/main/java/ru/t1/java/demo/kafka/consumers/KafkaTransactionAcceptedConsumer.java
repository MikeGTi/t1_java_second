package ru.t1.java.demo.kafka.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.service.impl.TransactionSecondServiceImpl;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionAcceptedConsumer<T extends TransactionDto> {

    private final TransactionSecondServiceImpl transactionSecondService;
    private final TransactionMapper transactionMapper;

    @KafkaListener(id = "${t1.kafka.topic.transaction-accept}",
                   topics = "${t1.kafka.topic.transaction-accept}",
                   containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<T> messageList,
                                  Acknowledgment ack) {

        log.debug("Transaction consumer: Обработка новых сообщений");
        try {
            List<Transaction> transactions = messageList.stream()
                                              .map(transactionMapper::toEntity)
                                              .toList();
            transactionSecondService.handle(transactions);
        } finally {
            ack.acknowledge();
        }
        log.debug("Transaction consumer: записи обработаны");
    }
}
