package ru.t1.java.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.service.ClientService;
import ru.t1.java.demo.service.ParserService;
import ru.t1.java.demo.service.HandleService;
import ru.t1.java.demo.util.ClientMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService, ParserService<Client>, HandleService<Client> {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    /*@PostConstruct
    void init() {
        List<Client> clients = null;
        try {
            clients = parseJson();
        } catch (IOException e) {
            log.error("Ошибка во время обработки записей", e);
        }
        if (clients != null) {
            clientRepository.saveAll(clients);
        }
    }*/

    @Override
//    @LogExecution
//    @Track
//    @HandlingResult
    public List<Client> parseJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClientDto[] clients = mapper.readValue(new File("src/main/resources/mock_data/client/client.json"),
                                               ClientDto[].class);
        return Arrays.stream(clients)
                .map(ClientMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void handle(Iterable<Client> clients) {
        clientRepository.saveAllAndFlush(clients);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    @LogDataSourceError
    @Override
    public Client findByClientUuid(UUID clientUuid) {
        Optional<Client> client = Optional.ofNullable(clientRepository.findByClientUuid(clientUuid));
        if (client.isEmpty()) {
            throw new ClientException(String.format("Client with uuid %s is not exists", clientUuid));
        }
        return client.get();
    }

    @Transactional(readOnly = true)
    @LogDataSourceError
    @Override
    public Client findByAccountUuid(UUID accountUuid) {
        Optional<Client> client = Optional.ofNullable(clientRepository.findByClientUuid(accountUuid));
        if (client.isEmpty()) {
            throw new ClientException(String.format("Account with uuid %s is not exists", accountUuid));
        }
        return client.get();
    }
    
    @LogDataSourceError
    @Transactional(readOnly = true)
    @Override
    public List<Account> findAccountsByClientUuid(UUID clientUuid) throws ClientException{
        Client client = clientRepository.findByClientUuid(clientUuid);
        return accountRepository.findAllByClient(client);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client saveClient(Client entity) {
        entity.getAccounts().forEach(account -> {
                account.setClient(entity);
                account.getTransactions().forEach(transaction -> transaction.setAccount(account));
            });
        return clientRepository.save(entity);
    }

    @LogDataSourceError
    @Override
    public void deleteClient(UUID clientUuid) throws ClientException {
        Optional<Client> client = Optional.ofNullable(clientRepository.findByClientUuid(clientUuid));
        client.ifPresent(clientRepository::delete);
    }

    @LogDataSourceError
    @Override
    public Client updateClient(UUID clientUuid, Client clientDto) throws ClientException{
        Optional<Client>  client = Optional.ofNullable(clientRepository.findByClientUuid(clientUuid));
        if(client.isEmpty()) {
            throw new ClientException(String.format("Client with uuid %s is not exists", clientUuid));
        }

        client.get().setFirstName(clientDto.getFirstName());
        client.get().setMiddleName(clientDto.getMiddleName());
        client.get().setLastName(clientDto.getLastName());
        clientDto.getAccounts().forEach(client.get()::addAccount);

        return clientRepository.save(client.get());
    }

}
