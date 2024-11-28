package ru.t1.java.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionHandlerService {

    @Transactional
    @LogDataSourceError
    void handle(List<Transaction> transactions);


}
