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
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.HandleService;


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

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final KafkaJsonMessageProducer producer;


    @Transactional
    @Override
    public void handle(Iterable<Transaction> entities) {
        // filter & set Transactions, accounts
        entities.forEach(transaction -> {
            Account account = accountRepository.findByAccountUuid(transaction.getAccountUuid());
            TransactionStatus transactionStatus = transaction.getStatus();

            if (transactionStatus.equals(TransactionStatus.ACCEPTED)) {
                transactionRepository.updateStatusByTransactionUuid(transaction.getTransactionUuid(), TransactionStatus.ACCEPTED);

            } else if (transactionStatus.equals(TransactionStatus.BLOCKED)) {
                transactionRepository.updateStatusByTransactionUuid(transaction.getTransactionUuid(), TransactionStatus.BLOCKED);
                accountRepository.updateStatusByAccountUuid(transaction.getAccountUuid(), AccountStatus.BLOCKED);
                // set new frozenAmount
                account.setFrozenAmount(account.getBalance().add(transaction.getAmount()));
                accountRepository.updateFrozenAmountByAccountUuid(account.getAccountUuid(), account.getFrozenAmount());

            } else if (transactionStatus.equals(TransactionStatus.REJECTED)) {
                transactionRepository.updateStatusByTransactionUuid(transaction.getTransactionUuid(), TransactionStatus.REJECTED);
                // set new balance
                account.setBalance(account.getBalance().add(transaction.getAmount()));
                accountRepository.updateBalanceByAccountUuid(account.getAccountUuid(), account.getBalance());

            } else if (account.getStatus().equals(AccountStatus.OPEN))  {
                transaction.setStatus(TransactionStatus.REQUESTED);
                transactionRepository.save(transaction);
                // set new balance
                account.setBalance(account.getBalance().add(transaction.getAmount()));
                // messaging
                Client client = account.getClient();
                String message = buildJsonMessage(transaction, account, client);
                producer.sendTo(topicToSend, message);
            }
        });
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