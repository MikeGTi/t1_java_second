package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.kafka.producers.KafkaTransactionProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.RegistrarService;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionRegistrarServiceImpl implements RegistrarService<Transaction> {

    @Value("${t1.kafka.topic.transaction-accept}")
    private String transactionAcceptedTopic;

    private final AccountServiceImpl accountService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private final KafkaTransactionProducer<TransactionDto> producer;
    private Map<UUID, Account> cache = new HashMap<>();

    @Transactional
    @Override
    public void register(Iterable<Transaction> entities) {
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

        // handle Accounts balance
        transactionList.forEach(transaction -> {
            // set new balance
            Account account = cache.get(transaction.getAccountUuid());
            account.setBalance(account.getBalance().add(transaction.getAmount()));
            // messaging
            producer.sendTo(transactionAcceptedTopic, transactionMapper.toDto(transaction));
        });

        transactionRepository.saveAllAndFlush(transactionList);
    }
}
