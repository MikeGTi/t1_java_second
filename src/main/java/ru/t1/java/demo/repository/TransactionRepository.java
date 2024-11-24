package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Transaction findById(Long transactionId);

    Transaction findByTransactionUuid(UUID transactionUuid);

    List<Transaction> findAllTransactionsByAccount(Account account);

    List<Transaction> findAllTransactionsByClient(Client client);

    @Override
    <S extends Transaction> List<S> saveAllAndFlush(Iterable<S> transactions);
}