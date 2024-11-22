package ru.t1.java.demo.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.Optional;


@Component
@AllArgsConstructor
public class TransactionMapper {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public Transaction toEntity(TransactionDto dto) throws TransactionException {
        Long accountUuid = dto.getAccountUuid();
        Optional<Account> account = accountRepository.findById(accountUuid);
        if (account.isEmpty()) {
            throw new TransactionException(String.format("Account with id %s is not exists", accountUuid));
        }
        Long clientUuid = dto.getClientUuid();
        Optional<Client> client = clientRepository.findById(clientUuid);
        if (client.isEmpty()) {
            throw new TransactionException(String.format("Client with id %s is not exists", clientUuid));
        }
        return Transaction.builder()
                .uuid(dto.getUuid())
                .account(account.get())
                .client(client.get())
                .amount(dto.getAmount())
                .status(dto.getStatus())
                .time(dto.getTimestamp())
                .build();
    }

    public TransactionDto toDto(Transaction entity) {
        return TransactionDto.builder()
                .uuid(entity.getUuid())
                .amount(entity.getAmount())
                .timestamp(entity.getTime())
                .accountUuid(entity.getAccount().getAccountUuid())
                .build();
    }
}
