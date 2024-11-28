package ru.t1.java.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.model.Transaction;

import java.util.List;

public interface TransactionHandlerService {

    @Transactional
    void handle(List<Transaction> transactions);


}
