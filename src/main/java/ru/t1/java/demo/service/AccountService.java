package ru.t1.java.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.model.Account;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    @LogDataSourceError
    List<Account> parseJson() throws IOException;

    @Transactional
    @LogDataSourceError
    void register(List<Account> accounts);

    @Transactional(readOnly = true)
    @LogDataSourceError
    Account getAccountsByAccountUuid(UUID accountUuid);

    @Transactional(readOnly = true)
    @LogDataSourceError
    List<Account> getAccountsByClientUuid(UUID clientUuid);

    @Transactional(readOnly = true)
    @LogDataSourceError
    List<Account> getAccountsByAccountUuid(List<UUID> accountUuids);

    @Transactional(readOnly = true)
    @LogDataSourceError
    List<Account> findAll();

    @Transactional
    @LogDataSourceError
    Account save(Account entity);

    @Transactional
    @LogDataSourceError
    void delete(UUID account_uuid) throws AccountException;
}
