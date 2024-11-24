package ru.t1.java.demo.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.Optional;
import java.util.UUID;


@Component
@AllArgsConstructor
public class TransactionMapper {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public Transaction toEntity(TransactionDto dto) throws TransactionException {
        UUID accountUuid = dto.getAccountUuid();
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountUuid(accountUuid));
        if (account.isEmpty()) {
            throw new TransactionException(String.format("Account with uuid %s is not exists", accountUuid));
        }
        UUID clientUuid = dto.getClientUuid();
        Optional<Client> client = Optional.ofNullable(clientRepository.findByClientUuid(clientUuid));
        if (client.isEmpty()) {
            throw new TransactionException(String.format("Client with uuid %s is not exists", clientUuid));
        }
        return Transaction.builder()
                .transactionUuid(dto.getTransactionUuid())
                .account(account.get())
                .client(client.get())
                .amount(dto.getAmount())
                .status(dto.getStatus())
                .created(dto.getCreated())
                .build();
    }

    public TransactionDto toDto(Transaction entity) {
        return TransactionDto.builder()
                .transactionUuid(entity.getTransactionUuid())
                .amount(entity.getAmount())
                .created(entity.getCreated())
                .accountUuid(entity.getAccount().getAccountUuid())
                .build();
    }
}
