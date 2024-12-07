package ru.t1.java.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.model.Account;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    List<Account> parseJson() throws IOException;

    void register(List<Account> accounts);

    Account getAccountsByAccountUuid(UUID accountUuid);

    List<Account> getAccountsByClientUuid(UUID clientUuid);

    List<Account> getAccountsByAccountUuid(List<UUID> accountUuids);

    List<Account> findAll();

    Account save(Account entity);

    void delete(UUID accountUuid) throws AccountException;
}
