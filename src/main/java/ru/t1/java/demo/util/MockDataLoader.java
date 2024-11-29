package ru.t1.java.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

@Setter
@Getter
@Component
@RequiredArgsConstructor
@Slf4j
public class MockDataLoader {

    private final ClientRepository clientRepository;

    @Value("${t1.mock-data.add-objects-counter}")
    private Integer counter;

    @Value("${t1.mock-data.account-file-path}")
    private String accountFilePath;

    @Value("${t1.mock-data.client-file-path}")
    private String clientFilePath;

    @Value("${t1.mock-data.transaction-file-path}")
    private String transactionFilePath;

    @Value("${t1.mock-data.full-data-file-path}")
    private String fullDataFilePath;

    /*@PostConstruct
    void init() {
        if (clientRepository.count() == 0) {
            try {
                log.info("Loading data from {}", fullDataFilePath);
                loadData(fullDataFilePath);
                *//*loadData(transactionFilePath);
                loadData(accountFilePath);
                loadData(clientFilePath);*//*
            } catch (IOException e) {
                log.error("Load data error", e);
            }
        }
    }*/

    public void loadData(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        InputStream inputStream = getClass().getResourceAsStream(filePath);

        List<Client> clients = objectMapper.readValue(inputStream, new TypeReference<>() {});
        clients.forEach(client -> client.getAccounts()
                                        .forEach(account -> {
                                                               account.setClient(client);
                                                               account.getTransactions()
                                                                      .forEach(transaction -> transaction.setAccount(account));
                                                           }));
        clientRepository.saveAll(clients);
    }

}
