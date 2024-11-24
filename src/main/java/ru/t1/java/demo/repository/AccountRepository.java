package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Account findById(Long accountId);

    Account findByAccountUuid(UUID accountUuid);

    List<Account> findAllByClient(Client client);

    @Override
    <S extends Account> List<S> saveAllAndFlush(Iterable<S> accounts);
}