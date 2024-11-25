package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.kafka.producers.KafkaJsonMessageProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.HandleService;

import java.util.*;

/**
 * Task 3 Service 1 (accounts cached):<p>
 * - listen messages from transactions topic;<p>
 * - setup status;<p>
 * - set account balance;<p>
 * - send messages to accepted transactions topic;
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionFirstServiceImpl implements HandleService<Transaction> {

    @Value("${t1.kafka.topic.transaction-accept}")
    private String topicToSend;

    private final AccountServiceImpl accountService;
    private final TransactionRepository transactionRepository;

    private final KafkaJsonMessageProducer producer;
    private Map<UUID, Account> cache = new HashMap<>();

    @Transactional
    @Override
    public void handle(Iterable<Transaction> entities) {
        // filter & set Transactions
        List<Transaction> transactionList = new ArrayList<>();

        entities.forEach(transaction -> {
            if(accountService.findByUuid(transaction.getAccountUuid()).getStatus().equals(AccountStatus.OPEN))  {
                transactionList.add(transaction);
            }
        });

        transactionList.stream()
                .filter(transaction ->
                            accountService.findByUuid(transaction.getAccountUuid())
                                          .getStatus()
                                          .equals(AccountStatus.OPEN))
                .forEach(transaction -> transaction.setStatus(TransactionStatus.REQUESTED));


        // collect Accounts
        List<UUID> accountsUuids = transactionList.stream().map(Transaction::getAccountUuid).toList();
        List<Account> accounts = accountsUuids.stream().map(accountService::findByUuid).toList();

        accounts.forEach(account -> cache.putIfAbsent(account.getAccountUuid(), account));

        // handle Accounts balance & send message
        transactionList.forEach(transaction -> {
            // set new balance
            Account account = cache.get(transaction.getAccountUuid());
            // !!! Transaction NOT check on non > 0 balance after !!!
            account.setBalance(account.getBalance().add(transaction.getAmount()));
            // messaging
            Client client = account.getClient();
            String message = buildJsonMessage(transaction, account, client);
            producer.sendTo(topicToSend, message);
        });

        transactionRepository.saveAllAndFlush(transactionList);
    }

    private String buildJsonMessage(Transaction transaction, Account account, Client client) {
        //{clientId, accountId, transactionId, timestamp, transaction.amount, account.balance}
        return String.format("{%s, %s, %s, %s, %s, %s}", client.getClientUuid(),
                                                         account.getAccountUuid(),
                                                         transaction.getTransactionUuid(),
                                                         transaction.getCreated(),
                                                         transaction.getAmount(),
                                                         account.getBalance());
    }
}